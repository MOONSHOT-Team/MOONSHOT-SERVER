package org.moonshot.server.domain.image.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class UnsupportedFileExtensionException extends MoonshotException {
    public UnsupportedFileExtensionException() {
        super(ErrorType.INVALID_IMAGE_EXTENSION);
    }
}
