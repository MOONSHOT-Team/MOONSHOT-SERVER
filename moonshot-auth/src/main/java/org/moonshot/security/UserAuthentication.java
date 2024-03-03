package org.moonshot.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAuthentication extends AbstractAuthenticationToken {

    private final UserDetails userPrincipal;

    public UserAuthentication(UserDetails userPrincipal) {
        super(userPrincipal.getAuthorities());
        super.setAuthenticated(true);
        this.userPrincipal = userPrincipal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userPrincipal;
    }
}