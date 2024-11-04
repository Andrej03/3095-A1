package ca.gbc.userservice.dto;

import ca.gbc.userservice.model.Users;

public record UserRequest(
        String name,
        String email,
        Users.Role role,
        Users.UserType userType
) { }
