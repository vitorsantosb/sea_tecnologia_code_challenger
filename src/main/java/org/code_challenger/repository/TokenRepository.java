package org.code_challenger.repository;

import org.code_challenger.repository.dto.TokenCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenCredentials, UUID> {

    @Modifying
    @Query("DELETE FROM TokenCredentials t WHERE t.expirationTime < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();

    Optional<TokenCredentials> findByToken(UUID token);
}