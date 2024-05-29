package org.moonshot.discord;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class SignUpEventListener {

    private final DiscordAppender discordAppender;

    @EventListener
    public void handleSignUpEvent(SignUpEvent event) {
        discordAppender.signInAppend(
                event.totalUserCount(),
                event.name(),
                event.email(),
                event.socialPlatform(),
                event.createdAt(),
                event.imageUrl());
    }

}
