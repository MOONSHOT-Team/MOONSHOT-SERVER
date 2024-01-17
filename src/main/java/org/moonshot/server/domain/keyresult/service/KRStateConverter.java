package org.moonshot.server.domain.keyresult.service;

import org.moonshot.server.domain.keyresult.model.KRState;
import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class KRStateConverter implements Converter<String, KRState> {

    @Override
    public KRState convert(String source) {
        for (KRState krState : KRState.values()) {
            if (krState.getValue().equals(source)) {
                return krState;
            }
        }
        throw new MoonshotException(ErrorType.INVALID_TYPE);
    }

}
