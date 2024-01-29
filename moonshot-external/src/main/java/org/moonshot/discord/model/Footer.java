package org.moonshot.discord.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Footer {
    
    private final String text;
    private final String iconUrl;
}
