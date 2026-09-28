package com.codecool.pawsandrequests.controller;

import com.codecool.pawsandrequests.dto.UserRequest;
import com.codecool.pawsandrequests.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public final class UserController {

    private final UserService userService;

    public UserController(final UserService service) {
        this.userService = service;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> editUser(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable final UUID id,
            @RequestBody final UserRequest request
    ) {
        userService.editUser(userDetails.getUsername(), id, request);
        return ResponseEntity.noContent().build();
    }
}
