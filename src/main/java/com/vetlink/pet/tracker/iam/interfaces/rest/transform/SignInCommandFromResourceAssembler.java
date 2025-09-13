package com.vetlink.pet.tracker.iam.interfaces.rest.transform;

import com.vetlink.pet.tracker.iam.domain.model.commands.SignInCommand;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource){
        return new SignInCommand(resource.username(), resource.password());
    }
}
