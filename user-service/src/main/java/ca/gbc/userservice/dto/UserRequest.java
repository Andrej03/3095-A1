package ca.gbc.userservice.dto;

import ca.gbc.userservice.model.Users;

public record UserRequest(
        Long id,
        String name,
        String email,
        Users.Role role,
        Users.UserType userType
) { }
