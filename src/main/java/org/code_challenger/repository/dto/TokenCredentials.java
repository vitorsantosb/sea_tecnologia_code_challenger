package org.code_challenger.repository.dto;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Data
public class TokenCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID token;

    private UUID userId;

    private LocalDateTime expirationTime;

}
