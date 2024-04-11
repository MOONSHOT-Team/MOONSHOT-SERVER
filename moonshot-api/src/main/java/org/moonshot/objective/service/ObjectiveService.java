package org.moonshot.objective.service;

import static org.moonshot.objective.service.validator.ObjectiveValidator.validateActiveObjectiveSizeExceeded;
import static org.moonshot.objective.service.validator.ObjectiveValidator.validateIndexWithInRange;
import static org.moonshot.response.ErrorType.INVALID_EXPIRE_AT;
import static org.moonshot.response.ErrorType.NOT_FOUND_OBJECTIVE;
import static org.moonshot.response.ErrorType.NOT_FOUND_USER;
import static org.moonshot.response.ErrorType.REQUIRED_EXPIRE_AT;
import static org.moonshot.user.service.validator.UserValidator.validateUserAuthorization;
import static org.moonshot.validator.IndexValidator.isIndexIncreased;
import static org.moonshot.validator.IndexValidator.isSameIndex;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.moonshot.common.model.Period;
import org.moonshot.exception.BadRequestException;
import org.moonshot.exception.NotFoundException;
import org.moonshot.keyresult.service.KeyResultService;
import org.moonshot.objective.dto.request.ModifyIndexRequestDto;
import org.moonshot.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.objective.dto.response.DashboardResponseDto;
import org.moonshot.objective.dto.response.HistoryResponseDto;
import org.moonshot.objective.dto.response.ObjectiveGroupByYearDto;
import org.moonshot.objective.model.Category;
import org.moonshot.objective.model.Criteria;
import org.moonshot.objective.model.IndexService;
import org.moonshot.objective.model.Objective;
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
        User user =  userRepository.findByIdWithCache(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));

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
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_OBJECTIVE));
        validateUserAuthorization(objective.getUser().getId(), userId);

        keyResultService.deleteKeyResult(objective);
        objectiveRepository.delete(objective);
        objectiveRepository.bulkUpdateIdxDecreaseAfter(objective.getIdx(), userId);

        return getObjectiveInDashboard(userId, null);
    }

    public void deleteAllObjective(final List<User> userList) {
        List<Objective> objectiveList = objectiveRepository.findAllByUserIn(userList);
        keyResultService.deleteAllKeyResult(objectiveList);
        objectiveRepository.deleteAllInBatch(objectiveList);
    }

    public void modifyObjective(final Long userId, final ModifyObjectiveRequestDto request) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(request.objectiveId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_OBJECTIVE));
        validateUserAuthorization(objective.getUser().getId(), userId);

        objective.modifyClosed(request.isClosed());
        if (!request.isClosed()) {
            if (request.expireAt() == null) {
                throw new BadRequestException(REQUIRED_EXPIRE_AT);
            } else if (request.expireAt().isBefore(LocalDate.now())) {
                throw new BadRequestException(INVALID_EXPIRE_AT);
            }
            objective.modifyPeriod(Period.of(objective.getPeriod().getStartAt(), request.expireAt()));
        }
    }

    @Transactional(readOnly = true)
    public DashboardResponseDto getObjectiveInDashboard(final Long userId, final Long objectiveId) {
        List<Objective> objList = objectiveRepository.findAllByUserId(userId);
        if (objList.isEmpty()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
            return DashboardResponseDto.ofNull(user.getNickname());
        }

        Long treeId = objectiveId == null ? objList.get(0).getId() : objectiveId;
        Objective objective = objectiveRepository.findByIdWithKeyResultsAndTasks(treeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_OBJECTIVE));
        validateUserAuthorization(objective.getUser().getId(), userId);

        return DashboardResponseDto.of(objective, objList, objective.getUser().getNickname());
    }

    @Transactional(readOnly = true)
    public HistoryResponseDto getObjectiveHistory(final Long userId, final Integer year, final Category category, final Criteria criteria) {
        List<Objective> objectives = objectiveRepository.findObjectives(userId, year, category, criteria);
        Map<Integer, List<Objective>> groups = objectives.stream()
                .collect(Collectors.groupingBy(objective -> objective.getPeriod().getStartAt().getYear()));
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

        return HistoryResponseDto.of(groupsSortedByCriteria, categories);
    }

    @Override
    public void modifyIdx(final ModifyIndexRequestDto request, final Long userId) {
        Long objectiveCount = objectiveRepository.countAllByUserId(userId);
        validateIndexWithInRange(objectiveCount, request.idx());

        Objective objective = objectiveRepository.findObjectiveAndUserById(request.id())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_OBJECTIVE));
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
