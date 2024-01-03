package org.moonshot.server.global.external.s3.exception;

import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;

public class UnsupportedFileExtensionException extends MoonshotException {

    public UnsupportedFileExtensionException() {
        super(ErrorType.INVALID_IMAGE_EXTENSION);
    }

}
