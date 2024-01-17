package org.moonshot.server.domain.objective.model.converter;

import org.moonshot.server.domain.objective.model.Category;
import org.moonshot.server.global.common.exception.MoonshotException;
import org.moonshot.server.global.common.response.ErrorType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    @Override
    public Category convert(String source) {
        for (Category category : Category.values()) {
            if (category.getValue().equals(source)) {
                return category;
            }
        }
        throw new MoonshotException(ErrorType.INVALID_TYPE);
    }

}
