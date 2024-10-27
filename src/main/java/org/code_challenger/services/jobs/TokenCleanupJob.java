package org.code_challenger.services.jobs;

import org.code_challenger.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class TokenCleanupJob {
    private TokenRepository tokenRepository;

    @Scheduled(fixedRate = 60000)
    public void cleanUpExpiredTokens() {
        System.out.println("Cleaning user expired tokens...");
        tokenRepository.deleteExpiredTokens();
    }
}
