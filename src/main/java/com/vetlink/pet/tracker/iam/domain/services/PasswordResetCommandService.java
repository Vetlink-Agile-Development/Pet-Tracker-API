package com.vetlink.pet.tracker.iam.domain.services;

import com.vetlink.pet.tracker.iam.domain.model.commands.RequestPasswordResetCommand;
import com.vetlink.pet.tracker.iam.domain.model.commands.ResetPasswordCommand;

public interface PasswordResetCommandService {
    void handle(RequestPasswordResetCommand command);
    void handle(ResetPasswordCommand command);
}
