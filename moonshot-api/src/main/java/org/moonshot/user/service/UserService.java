package org.moonshot.user.service;

import static org.moonshot.response.ErrorType.NOT_FOUND_USER;
import static org.moonshot.response.ErrorType.NOT_SUPPORTED_LOGIN_PLATFORM;
import static org.moonshot.user.service.validator.UserValidator.hasChange;
import static org.moonshot.user.service.validator.UserValidator.validateUserAuthorization;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.moonshot.exception.BadRequestException;
import org.moonshot.exception.NotFoundException;
import org.moonshot.jwt.JwtTokenProvider;
import org.moonshot.jwt.TokenResponse;
import org.moonshot.objective.service.ObjectiveService;
import org.moonshot.user.dto.request.SocialLoginRequest;
import org.moonshot.user.dto.request.UserInfoRequest;
import org.moonshot.user.dto.response.SocialLoginResponse;
import org.moonshot.user.dto.response.UserInfoResponse;
import org.moonshot.user.model.User;
import org.moonshot.user.repository.UserRepository;
import org.moonshot.user.service.social.SocialLoginContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ObjectiveService objectiveService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialLoginContext socialLoginContext;

    public SocialLoginResponse login(final SocialLoginRequest request) {
        if (socialLoginContext.support(request.socialPlatform())) {
            return socialLoginContext.doLogin(request);
        }
        throw new BadRequestException(NOT_SUPPORTED_LOGIN_PLATFORM);
    }

    public TokenResponse reissue(final String refreshToken) {
        String token = refreshToken.substring("Bearer ".length());
        Long userId = jwtTokenProvider.validateRefreshToken(token);
        jwtTokenProvider.deleteRefreshToken(userId);
        return jwtTokenProvider.reissuedToken(userId);
    }

    public void logout(final Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        validateUserAuthorization(user.getId(), userId);

        jwtTokenProvider.deleteRefreshToken(userId);
    }

    public void withdrawal(final Long userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        validateUserAuthorization(user.getId(), userId);

        user.setDeleteAt();
    }

    public void modifyProfile(final Long userId, final UserInfoRequest request) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        validateUserAuthorization(user.getId(), userId);

        if (hasChange(request.nickname())) {
            user.modifyNickname(request.nickname());
        }
        if (hasChange(request.description())) {
            user.modifyDescription(request.description());
        }
    }

    public UserInfoResponse getMyProfile(final Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        return UserInfoResponse.of(user);
    }

    public void updateUserProfileImage(final Long userId, final String imageUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        user.modifyProfileImage(imageUrl);
    }

    public void softDeleteUser(LocalDateTime currentDate) {
        List<User> expiredUserList = userRepository.findIdByDeletedAtBefore(currentDate);
        if(!expiredUserList.isEmpty()) {
            objectiveService.deleteAllObjective(expiredUserList);
            userRepository.deleteAllInBatch(expiredUserList);
        }
    }

}