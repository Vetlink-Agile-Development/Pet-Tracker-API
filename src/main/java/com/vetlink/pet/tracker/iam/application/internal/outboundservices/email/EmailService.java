package com.vetlink.pet.tracker.iam.application.internal.outboundservices.email;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String username, String resetToken);
}
