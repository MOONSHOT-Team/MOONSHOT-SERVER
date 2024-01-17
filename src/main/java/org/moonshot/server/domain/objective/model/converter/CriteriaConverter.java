package org.moonshot.server.domain.objective.model.converter;

import org.moonshot.server.domain.objective.model.Criteria;
import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CriteriaConverter implements Converter<String, Criteria> {

    @Override
    public Criteria convert(String source) {
        for (Criteria criteria : Criteria.values()) {
            if (criteria.getValue().equals(source)) {
                return criteria;
            }
        }
        throw new MoonshotException(ErrorType.INVALID_TYPE);
    }

}
