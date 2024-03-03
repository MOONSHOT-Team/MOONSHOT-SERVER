package org.moonshot.security.service;

import lombok.RequiredArgsConstructor;
import org.moonshot.exception.user.UnauthorizedException;
import org.moonshot.user.model.UserPrincipal;
import org.moonshot.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(Long.parseLong(username))
                .map(UserPrincipal::new)
                .orElseThrow(UnauthorizedException::new);
    }

}
