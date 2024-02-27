package org.moonshot.exception.objective;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.response.ErrorType;

public class ObjectiveNotFoundException extends MoonshotException {

    public ObjectiveNotFoundException() {
        super(ErrorType.NOT_FOUND_OBJECTIVE_ERROR);
    }

}
