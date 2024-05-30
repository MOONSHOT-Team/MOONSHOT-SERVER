package org.moonshot.objective.dto.response.social;

import org.moonshot.objective.model.Objective;
import org.moonshot.user.model.User;

public record SocialOKRResponseDto(
    String category,
    String userName,
    String userImg,
    Long like,
    String userIntro,
    SocialObjectiveDto okrTreeData
) {
    public static SocialOKRResponseDto of(Objective objective, User user, SocialObjectiveDto okrTreeData) {
        return new SocialOKRResponseDto(
                objective.getCategory().getValue(),
                user.getName(),
                user.getImageUrl(),
                objective.getHeartCount(),
                user.getDescription(),
                okrTreeData
        );
    }

}

