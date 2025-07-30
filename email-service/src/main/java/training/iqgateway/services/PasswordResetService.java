package training.iqgateway.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import training.iqgateway.entities.PasswordResetToken;
import training.iqgateway.repositories.PasswordResetTokenRepository;
import training.iqgateway.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.iqgateway.entities.User;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
public class PasswordResetService {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_EXPIRY_HOURS = 1;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public boolean initiatePasswordReset(String email) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                logger.warn("Password reset attempted for non-existent email: {}", email);
                return false;
            }
            
            User user = userOpt.get();
            
            // Delete any existing tokens for this user
            tokenRepository.deleteByEmail(email);
            
            // Generate new token
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryTime = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);
            
            PasswordResetToken resetToken = new PasswordResetToken(token, user, email, expiryTime);
            tokenRepository.save(resetToken);
            
            // Send email
            emailService.sendPasswordResetEmail(email, token, user.getUsername());
            
            logger.info("Password reset initiated for user: {}", email);
            return true;
            
        } catch (Exception e) {
            logger.error("Error initiating password reset for email: {}", email, e);
            return false;
        }
    }
    
    public boolean resetPassword(String token, String newPassword) {
        try {
            Optional<PasswordResetToken> tokenOpt = tokenRepository.findByTokenAndUsedFalse(token);
            
            if (tokenOpt.isEmpty()) {
                logger.warn("Invalid or already used reset token: {}", token);
                return false;
            }
            
            PasswordResetToken resetToken = tokenOpt.get();
            
            if (resetToken.isExpired()) {
                logger.warn("Expired reset token used: {}", token);
                tokenRepository.delete(resetToken);
                return false;
            }
            
            Optional<User> userOpt = userRepository.findById(resetToken.getUserId().getId());
            if (userOpt.isEmpty()) {
                logger.error("User not found for reset token: {}", token);
                return false;
            }
            
            User user = userOpt.get();
            
            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            
            // Mark token as used
            resetToken.setUsed(true);
            tokenRepository.save(resetToken);
            
            // Send confirmation email
            emailService.sendPasswordResetConfirmationEmail(user.getEmail(), user.getUsername());
            
            logger.info("Password reset successful for user: {}", user.getEmail());
            return true;
            
        } catch (Exception e) {
            logger.error("Error resetting password for token: {}", token, e);
            return false;
        }
    }
    
    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByTokenAndUsedFalse(token);
        
        if (tokenOpt.isEmpty()) {
            return false;
        }
        
        PasswordResetToken resetToken = tokenOpt.get();
        return !resetToken.isExpired();
    }
    
    // Cleanup expired tokens (can be called by a scheduled task)
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiresAtBefore(LocalDateTime.now());
        logger.info("Cleaned up expired password reset tokens");
    }
}
