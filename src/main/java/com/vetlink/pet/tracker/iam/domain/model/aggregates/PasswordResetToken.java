package com.vetlink.pet.tracker.iam.domain.model.aggregates;

import com.vetlink.pet.tracker.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends AuditableAbstractAggregateRoot<PasswordResetToken> {

    @Getter
    @Column(nullable = false, unique = true)
    private String token;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Getter
    @Column(nullable = false)
    private boolean used;

    public PasswordResetToken() {
        this.used = false;
    }

    public PasswordResetToken(String token, User user, int expirationMinutes) {
        this();
        this.token = token;
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(expirationMinutes);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    public boolean isValid() {
        return !this.used && !isExpired();
    }

    public void markAsUsed() {
        this.used = true;
    }
}
