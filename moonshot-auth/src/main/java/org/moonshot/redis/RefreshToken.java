package org.moonshot.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
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