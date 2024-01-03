package org.moonshot.server.global.external.discord.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Author {

    private final String name;
    private final String url;
    private final String iconUrl;
}
