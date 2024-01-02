package org.moonshot.server.global.common.discord.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class ErrorLogAppenderException extends MoonshotException {
    public ErrorLogAppenderException() {
        super(ErrorType.DISCORD_LOG_APPENDER_ERROR);
    }
}
