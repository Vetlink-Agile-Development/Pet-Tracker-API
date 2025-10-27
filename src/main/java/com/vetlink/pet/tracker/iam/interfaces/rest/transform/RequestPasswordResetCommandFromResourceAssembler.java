package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.commands.RequestPasswordResetCommand;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.RequestPasswordResetResource;

public class RequestPasswordResetCommandFromResourceAssembler {
    public static RequestPasswordResetCommand toCommandFromResource(RequestPasswordResetResource resource) {
        return new RequestPasswordResetCommand(resource.email());
    }
}
