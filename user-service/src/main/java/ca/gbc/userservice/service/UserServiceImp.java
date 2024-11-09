package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.Users;
import ca.gbc.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        Users user = Users.builder()
                .name(userRequest.name())
                .email(userRequest.email())
                .role(userRequest.role())
                .userType(userRequest.userType())
                .build();

        userRepository.save(user);
        log.info("New user created: {}", user);
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getUserType());
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getUserType()))
                .toList();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        return user != null ? new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getUserType()) : null;
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        user.setName(userRequest.name());
        user.setEmail(userRequest.email());
        user.setRole(userRequest.role());
        user.setUserType(userRequest.userType());

        userRepository.save(user);

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getUserType());
    }

    @Override
    public void deleteUser(Long userId) {
        log.debug("Deleting user with id {}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public String getUserRole(Long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getRole().name() : null;
    }
}
