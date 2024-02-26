package org.moonshot.user.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Transactional
public class UserDeleteScheduler {

    private final UserService userService;

    @Scheduled(cron="0/10 * * * * *")   //10초에 한번씩 실행
    public void deleteUser() {
        userService.softDeleteUser(LocalDateTime.now());
    }
}
