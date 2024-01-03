package org.moonshot.server.global.external.discord.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Footer {
    
    private final String text;
    private final String iconUrl;
}
