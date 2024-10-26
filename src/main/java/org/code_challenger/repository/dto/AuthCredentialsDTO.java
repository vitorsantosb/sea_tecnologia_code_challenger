package org.code_challenger.repository.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthCredentialsDTO {
    private UUID id;
    private String userEmail;
    private String token;
    private String role;

    public AuthCredentialsDTO(UUID id, String userEmail, String token, String role) {
        this.id = id;
        this.userEmail = userEmail;
        this.token = token;
        this.role = role;
    }
}
