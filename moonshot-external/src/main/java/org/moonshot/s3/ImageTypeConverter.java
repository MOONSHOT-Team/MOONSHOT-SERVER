package org.moonshot.s3;

import org.moonshot.exception.BadRequestException;
import org.moonshot.response.ErrorType;
import org.springframework.core.convert.converter.Converter;

public class ImageTypeConverter implements Converter<String, ImageType> {

    @Override
    public ImageType convert(String source) {
        for (ImageType imageType : ImageType.values()) {
            if (imageType.getValue().equals(source)) {
                return imageType;
            }
        }
        throw new BadRequestException(ErrorType.INVALID_TYPE);
    }

}
