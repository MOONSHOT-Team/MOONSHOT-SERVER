package org.moonshot.user.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.moonshot.discord.SignUpEvent;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSignUpService {

    private final ApplicationEventPublisher eventPublisher;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishSignUpEvent(User user) {
        eventPublisher.publishEvent(SignUpEvent.of(
                user.getId(),
                user.getName(),
                user.getEmail() == null ? "" : user.getEmail(),
                user.getSocialPlatform().toString(),
                LocalDateTime.now(),
                user.getImageUrl()
        ));
    }

}
