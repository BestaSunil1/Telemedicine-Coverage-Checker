package training.iqgateway.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import training.iqgateway.entities.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByTokenAndUsedFalse(String token);
    void deleteByUserId(String userId);
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
    void deleteByEmail(String email);
}
