package org.moonshot.server.global.external.discord.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Field {

    private final String name;
    private final String value;
    private final boolean inline;
}
