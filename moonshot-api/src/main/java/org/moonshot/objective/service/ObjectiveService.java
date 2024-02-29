package org.moonshot.objective.service;

import static org.moonshot.objective.service.validator.ObjectiveValidator.*;
import static org.moonshot.user.service.validator.UserValidator.*;
import static org.moonshot.validator.IndexValidator.isIndexIncreased;
import static org.moonshot.validator.IndexValidator.isSameIndex;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.moonshot.common.model.Period;
import org.moonshot.exception.objective.DateInputRequiredException;
import org.moonshot.exception.objective.InvalidExpiredAtException;
import org.moonshot.exception.objective.ObjectiveNotFoundException;
import org.moonshot.exception.user.UserNotFoundException;
import org.moonshot.keyresult.service.KeyResultService;
import org.moonshot.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.objective.dto.response.DashboardResponseDto;
import org.moonshot.objective.dto.response.HistoryResponseDto;
import org.moonshot.objective.dto.response.ObjectiveGroupByYearDto;
import org.moonshot.objective.model.*;
import org.moonshot.objective.repository.ObjectiveRepository;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ObjectiveService implements IndexService {

    private final KeyResultService keyResultService;
    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;

    public void createObjective(final Long userId, final OKRCreateRequestDto request) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Objective> objectives = objectiveRepository.findAllByUserId(userId);
        validateActiveObjectiveSizeExceeded(objectives.size());
        objectiveRepository.bulkUpdateIdxIncrease(userId);

        Objective newObjective = objectiveRepository.save(Objective.builder()
                .title(request.objTitle())
                .category(request.objCategory())
                .content(request.objContent())
                .period(Period.of(request.objStartAt(), request.objExpireAt()))
                .user(user).build());

        keyResultService.createInitKRWithObjective(newObjective, request.krList());
    }

    public DashboardResponseDto deleteObjective(final Long userId, final Long objectiveId) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(objectiveId)
                .orElseThrow(ObjectiveNotFoundException::new);
        validateUserAuthorization(objective.getUser().getId(), userId);

        keyResultService.deleteKeyResult(objective);
        objectiveRepository.delete(objective);
        return getObjectiveInDashboard(userId, null);
    }

    public void deleteAllObjective(final List<User> userList) {
        List<Objective> objectiveList = objectiveRepository.findAllByUserIn(userList);
        keyResultService.deleteAllKeyResult(objectiveList);
        objectiveRepository.deleteAllInBatch(objectiveList);
    }

    public void modifyObjective(final Long userId, final ModifyObjectiveRequestDto request) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(request.objectiveId())
                .orElseThrow(ObjectiveNotFoundException::new);
        validateUserAuthorization(objective.getUser().getId(), userId);

        objective.modifyClosed(request.isClosed());
        if (!request.isClosed()) {
            if (request.expireAt() == null) {
                throw new DateInputRequiredException();
            } else if (request.expireAt().isBefore(LocalDate.now())) {
                throw new InvalidExpiredAtException();
            }
            objective.modifyPeriod(Period.of(objective.getPeriod().getStartAt(), request.expireAt()));
        }
    }

    @Transactional(readOnly = true)
    public DashboardResponseDto getObjectiveInDashboard(final Long userId, final Long objectiveId) {
        List<Objective> objList = objectiveRepository.findAllByUserId(userId);
        if (objList.isEmpty()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new);
            return DashboardResponseDto.ofNull(user.getNickname());
        }

        Long treeId = objectiveId == null ? objList.get(0).getId() : objectiveId;
        Objective objective = objectiveRepository.findByIdWithKeyResultsAndTasks(treeId)
                .orElseThrow(ObjectiveNotFoundException::new);
        validateUserAuthorization(objective.getUser().getId(), userId);

        return DashboardResponseDto.of(objective, objList, objective.getUser().getNickname());
    }

    @Transactional(readOnly = true)
    public HistoryResponseDto getObjectiveHistory(final Long userId, final Integer year, final Category category, final Criteria criteria) {
        List<Objective> objectives = objectiveRepository.findObjectives(userId, year, category, criteria);
        Map<Integer, List<Objective>> groups = objectives.stream()
                .collect(Collectors.groupingBy(objective -> objective.getPeriod().getStartAt().getYear()));
        Map<Integer, Integer> years = groups.entrySet().stream()
                .collect(Collectors.toMap(
                        Entry::getKey,
                        entry -> entry.getValue().size()
                ));
        List<String> categories = objectives.stream().map(objective -> objective.getCategory().getValue()).toList();

        List<ObjectiveGroupByYearDto> groupList = groups.entrySet().stream()
                .map(entry -> ObjectiveGroupByYearDto.of(entry.getKey(), entry.getValue())).toList();

        List<ObjectiveGroupByYearDto> groupsSortedByCriteria;
        if (criteria != null && criteria.equals(Criteria.LATEST)) {
            groupsSortedByCriteria = groupList.stream()
                    .sorted(Comparator.comparingInt(ObjectiveGroupByYearDto::year).reversed()).toList();
        } else if (criteria != null && criteria.equals(Criteria.OLDEST)) {
            groupsSortedByCriteria = groupList.stream()
                    .sorted(Comparator.comparingInt(ObjectiveGroupByYearDto::year)).toList();
        } else {
            groupsSortedByCriteria = groupList.stream()
                    .sorted(Comparator.comparingInt(ObjectiveGroupByYearDto::year).reversed()).toList();
        }

        return HistoryResponseDto.of(groupsSortedByCriteria, years, categories, criteria);
    }

    @Override
    public void modifyIdx(final ModifyIndexRequestDto request, final Long userId) {
        Long objectiveCount = objectiveRepository.countAllByUserId(userId);
        validateIndexWithInRange(objectiveCount, request.idx());

        Objective objective = objectiveRepository.findObjectiveAndUserById(request.id())
                .orElseThrow(ObjectiveNotFoundException::new);
        validateUserAuthorization(objective.getUser().getId(), userId);

        int prevIdx = objective.getIdx();
        if (isSameIndex(prevIdx, request.idx())) {
            return;
        }

        objective.modifyIdx(request.idx());
        if (isIndexIncreased(prevIdx, request.idx())) {
            objectiveRepository.bulkUpdateIdxDecrease(prevIdx + 1, request.idx(), userId, objective.getId());
        } else {
            objectiveRepository.bulkUpdateIdxIncrease(request.idx(), prevIdx, userId, objective.getId());
        }
    }

}
