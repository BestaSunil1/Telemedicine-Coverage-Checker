package training.iqgateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import training.iqgateway.services.PasswordResetService;

@Configuration
@EnableScheduling
public class ScheduledTasks {
    
    @Autowired
    private PasswordResetService passwordResetService;
    
    // Run every hour to cleanup expired tokens
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void cleanupExpiredTokens() {
        passwordResetService.cleanupExpiredTokens();
    }
}
