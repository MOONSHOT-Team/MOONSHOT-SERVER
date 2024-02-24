package org.moonshot.keyresult.service.converter;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.keyresult.model.KRState;
import org.moonshot.response.ErrorType;
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
