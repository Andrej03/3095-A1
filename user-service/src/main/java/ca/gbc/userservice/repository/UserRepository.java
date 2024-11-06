package ca.gbc.userservice.repository;

import ca.gbc.userservice.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findByEmailAndName(String email, String name);
}
