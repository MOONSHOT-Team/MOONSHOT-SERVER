package org.moonshot.exception.global.external.discord;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class ErrorLogAppenderException extends MoonshotException {
    public ErrorLogAppenderException() {
        super(ErrorType.DISCORD_LOG_APPENDER_ERROR);
    }
}
