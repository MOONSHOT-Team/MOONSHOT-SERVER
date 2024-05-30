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
    public static SocialOKRResponseDto of(Objective objective) {
        return new SocialOKRResponseDto(
                objective.getCategory().getValue(),
                objective.getUser().getNickname(),
                objective.getUser().getImageUrl(),
                objective.getHeartCount(),
                objective.getUser().getDescription(),
                SocialObjectiveDto.of(objective)
        );
    }

}

