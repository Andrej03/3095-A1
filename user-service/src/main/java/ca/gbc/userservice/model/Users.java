package ca.gbc.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public enum Role {
        STUDENT,
        STAFF,
        FACULTY
    }

    public enum UserType {
        STUDENT,
        STAFF,
        FACULTY
    }
}
