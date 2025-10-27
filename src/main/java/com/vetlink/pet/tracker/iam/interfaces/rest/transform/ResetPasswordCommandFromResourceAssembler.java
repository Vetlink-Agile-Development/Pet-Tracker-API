package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.commands.ResetPasswordCommand;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.ResetPasswordResource;

public class ResetPasswordCommandFromResourceAssembler {
    public static ResetPasswordCommand toCommandFromResource(ResetPasswordResource resource) {
        return new ResetPasswordCommand(resource.token(), resource.newPassword());
    }
}
