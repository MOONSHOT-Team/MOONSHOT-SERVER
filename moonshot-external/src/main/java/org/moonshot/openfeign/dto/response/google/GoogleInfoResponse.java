package org.moonshot.openfeign.dto.response.google;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GoogleInfoResponse(
        String sub,
        String name,
        String givenName,
        String familyName,
        String imageUrl,
        String email,
        Boolean emailVerified,
        String locale
) {
    public static GoogleInfoResponse of(String sub, String name, String givenName, String familyName, String imageUrl, String email, Boolean emailVerified, String locale) {
        return new GoogleInfoResponse(sub, name, givenName, familyName, imageUrl, email, emailVerified, locale);
    }
}