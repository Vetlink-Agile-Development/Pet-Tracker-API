package com.vetlink.pet.tracker.monitoring.application.internal.outboundservices.acl;

import com.vetlink.pet.tracker.iam.interfaces.acl.IamContextFacade;
import com.vetlink.pet.tracker.monitoring.domain.model.valueobjects.UserId;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ExternalIamService {

    private final IamContextFacade iamContextFacade;

    public ExternalIamService(IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public Optional<UserId> fetchUserIdByUsername(String username){
        var userId = iamContextFacade.fetchUserIdByUsername(username);
        if (userId == 0L) return Optional.empty();
        return Optional.of(new UserId(userId));
    }

    public Optional<UserId> createUser(String username, String password, String email, String firstName, String lastName) {
        var userId = iamContextFacade.createUser(username, email, firstName, lastName,password);
        if (userId == 0L) return Optional.empty();
        return Optional.of(new UserId(userId));
    }

    public Optional<UserId> fetchUsernameById(Long userId) {
        var username = iamContextFacade.fetchUsernameByUserId(userId);
        if (username.equals(Strings.EMPTY)) return Optional.empty();
        return Optional.of(new UserId(userId));
    }
}
