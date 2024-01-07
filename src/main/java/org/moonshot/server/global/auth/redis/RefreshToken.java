package org.moonshot.server.global.auth.redis;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.moonshot.server.global.constants.JWTConstants;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 60 * 1000L * 60 * 24 * 7 * 2)
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