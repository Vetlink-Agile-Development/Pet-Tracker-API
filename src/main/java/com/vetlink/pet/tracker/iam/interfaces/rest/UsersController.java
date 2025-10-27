package com.vetlink.pet.tracker.iam.interfaces.rest;

import com.vetlink.pet.tracker.iam.domain.model.queries.GetAllUsersQuery;
import com.vetlink.pet.tracker.iam.domain.model.queries.GetUserByIdQuery;
import com.vetlink.pet.tracker.iam.domain.services.UserCommandService;
import com.vetlink.pet.tracker.iam.domain.services.UserQueryService;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.UpdateUserResource;
import com.vetlink.pet.tracker.iam.interfaces.rest.resource.UserResource;
import com.vetlink.pet.tracker.iam.interfaces.rest.transform.UpdateUserCommandFromResourceAssembler;
import com.vetlink.pet.tracker.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User Management Endpoints")
public class UsersController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);
        var userResource =users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return new ResponseEntity<>(userResource, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResource> updateUser(@PathVariable Long userId, @RequestBody UpdateUserResource resource) {
        var updateUserCommand = UpdateUserCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var user = userCommandService.handle(updateUserCommand);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.OK);
    }

}
