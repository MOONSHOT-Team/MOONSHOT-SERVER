package org.moonshot.exception.global.external.s3;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class UnsupportedFileExtensionException extends MoonshotException {

    public UnsupportedFileExtensionException() {
        super(ErrorType.INVALID_IMAGE_EXTENSION);
    }

}
