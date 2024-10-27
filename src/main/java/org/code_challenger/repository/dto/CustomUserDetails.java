package org.code_challenger.repository.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Data
public class CustomUserDetails {
    private UUID userId;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UUID _userId, String _email, Collection<? extends GrantedAuthority> authorities) {
        this.userId = _userId;
        this.email = _email;
        this.authorities = authorities;
    }
}
