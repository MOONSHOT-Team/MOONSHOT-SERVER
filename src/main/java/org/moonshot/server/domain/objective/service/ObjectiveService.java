package org.moonshot.server.domain.objective.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.server.domain.keyresult.service.KeyResultService;
import org.moonshot.server.domain.objective.dto.request.OKRCreateRequestDto;
import org.moonshot.server.domain.objective.exception.ObjectiveNumberExceededException;
import org.moonshot.server.domain.objective.model.Objective;
import org.moonshot.server.domain.objective.repository.ObjectiveRepository;
import org.moonshot.server.domain.user.exception.UserNotFoundException;
import org.moonshot.server.domain.user.model.User;
import org.moonshot.server.domain.user.repository.UserRepository;
import org.moonshot.server.global.common.model.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ObjectiveService {

    private static final int ACTIVE_OBJECTIVE_NUMBER = 10;

    private final KeyResultService keyResultService;
    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;

    @Transactional
    public void create(OKRCreateRequestDto request, String nickname) {
        User findUser = userRepository.findUserByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);

        if (objectiveRepository.countAllByUserAndIsClosed(findUser, false) >= ACTIVE_OBJECTIVE_NUMBER) {
            throw new ObjectiveNumberExceededException();
        }

        Objective newObjective = objectiveRepository.save(Objective.builder()
                .title(request.objTitle())
                .category(request.objCategory())
                .content(request.objContent())
                .period(Period.of(request.objStartAt(), request.objExpireAt()))
                .user(findUser).build());

        keyResultService.createInitWithObjective(newObjective, request.krList());
    }

}
