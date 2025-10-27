package com.vetlink.pet.tracker.iam.infrastructure.persistence.jpa.repositories;

import com.vetlink.pet.tracker.iam.domain.model.aggregates.PasswordResetToken;
import com.vetlink.pet.tracker.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUserAndUsedFalseAndExpiryDateAfter(User user, LocalDateTime now);
    void deleteByExpiryDateBefore(LocalDateTime now);
    void deleteByUser(User user);
}
