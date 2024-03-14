package org.moonshot.user.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.s3.ImageEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ImageEventListener {

    private final UserService userService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleImageEvent(ImageEvent imageEvent) {
        userService.updateUserProfileImage(imageEvent.userId(), imageEvent.imageUrl());
    }

}
