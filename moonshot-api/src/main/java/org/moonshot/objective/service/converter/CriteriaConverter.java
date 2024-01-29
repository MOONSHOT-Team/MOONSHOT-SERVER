package org.moonshot.objective.service.converter;

import org.moonshot.exception.global.common.MoonshotException;
import org.moonshot.objective.model.Criteria;
import org.moonshot.response.ErrorType;
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
