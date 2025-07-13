package com.ventinc.futbol_ranker.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    // Actual user entity
    private final Users user;

    public UserPrincipal(Users user) {
        this.user = user;
    }

    // Convert a user role to Spring Security authority format
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    // Return the hashed password of the user
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Return username for authentication
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // Account status methods
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}
