package org.moonshot.server.global.auth.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import static org.moonshot.server.global.constants.JWTConstants.REFRESH_TOKEN_EXPIRATION_TIME;

@RedisHash(value = "refreshToken", timeToLive = REFRESH_TOKEN_EXPIRATION_TIME)
@AllArgsConstructor
@Getter
@Builder
public class RefreshToken {

    @Id
    private Long id;
    private String refreshToken;

    public static RefreshToken of(
            final Long id,
            final String refreshToken
    ) {
        return RefreshToken.builder()
                .id(id)
                .refreshToken(refreshToken)
                .build();
    }

}