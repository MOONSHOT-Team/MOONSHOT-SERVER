package org.moonshot.server.domain.objective.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.service.KeyResultService;
import org.moonshot.server.domain.objective.dto.request.ModifyObjectiveRequestDto;
import org.moonshot.server.domain.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.server.domain.objective.dto.response.DashboardResponseDto;
import org.moonshot.server.domain.objective.exception.InvalidExpiredAtException;
import org.moonshot.server.domain.objective.exception.ObjectiveNotFoundException;
import org.moonshot.server.domain.objective.exception.ObjectiveNumberExceededException;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.objective.repository.ObjectiveRepository;
import org.moonshot.server.domain.user.exception.UserNotFoundException;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.domain.user.repository.UserRepository;
import org.moonshot.server.domain.user.service.UserService;
import org.moonshot.server.global.auth.exception.AccessDeniedException;
import org.moonshot.server.global.common.model.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjectiveService {

    private static final int ACTIVE_OBJECTIVE_NUMBER = 10;

    private final UserService userService;
    private final KeyResultService keyResultService;
    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;

    @Transactional
    public void createObjective(Long userId, OKRCreateRequestDto request) {
        User user =  userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (objectiveRepository.countAllByUserAndIsClosed(user, false) >= ACTIVE_OBJECTIVE_NUMBER) {
            throw new ObjectiveNumberExceededException();
        }

        Objective newObjective = objectiveRepository.save(Objective.builder()
                .title(request.objTitle())
                .category(request.objCategory())
                .content(request.objContent())
                .period(Period.of(request.objStartAt(), request.objExpireAt()))
                .user(user).build());

        keyResultService.createInitKRWithObjective(newObjective, request.krList());
    }

    @Transactional
    public void deleteObjective(Long userId, Long objectiveId) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(objectiveId)
                .orElseThrow(ObjectiveNotFoundException::new);
        if (!objective.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }
        keyResultService.deleteKeyResult(objective);
        objectiveRepository.delete(objective);
    }

    @Transactional
    public void modifyObjective(Long userId, ModifyObjectiveRequestDto request) {
        Objective objective = objectiveRepository.findObjectiveAndUserById(request.objectiveId())
                .orElseThrow(ObjectiveNotFoundException::new);
        userService.validateUserAuthorization(objective.getUser(), userId);
        objective.modifyClosed(request.isClosed());
        if (!request.isClosed()) {
            if (request.expireAt().isBefore(LocalDateTime.now())) {
                throw new InvalidExpiredAtException();
            }
            objective.modifyPeriod(Period.of(objective.getPeriod().getStartAt(), request.expireAt()));
        }
    }

    public DashboardResponseDto getObjectiveInDashboard(Long userId) {
        List<Objective> objList = objectiveRepository.findAllByUserId(userId);
        Objective objective = objectiveRepository.findByIdWithKeyResultsAndTasks(objList.get(0).getId())
                .orElseThrow(ObjectiveNotFoundException::new);
        if (!objective.getUser().getId().equals(userId)) {
            throw new AccessDeniedException();
        }

        return DashboardResponseDto.of(objective, objList);
    }

}
