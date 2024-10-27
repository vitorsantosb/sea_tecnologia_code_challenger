package org.code_challenger.services.jobs;

import org.code_challenger.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@EnableScheduling
public class TokenCleanupJob {
    private final TokenRepository tokenRepository;

    @Autowired
    public TokenCleanupJob(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanUpExpiredTokens() {
        System.out.println("Cleaning user expired tokens...");
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteExpiredTokens(now);
    }
}
