package ca.gbc.userservice.dto;

import ca.gbc.userservice.model.Users;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public record UserRequest(
        String name,
        String email,
        Users.Role role,
        Users.UserType userType
) { }
