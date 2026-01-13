package com.masa.appointment.user.service;

import com.masa.appointment.user.entity.Role;
import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.user.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerCustomer(String fullName, String email, String rawPassword) {

    if (userRepository.findByEmail(email).isPresent()) {
        throw new RuntimeException("Email already exists");
    }

    UserEntity user = new UserEntity();
    user.setFullName(fullName);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(rawPassword));
    user.setRole(Role.CUSTOMER);

    return userRepository.save(user);
}


    public UserEntity create(String fullName, String email,  String password,
                                    String roleStr) {

         Role role;

        if (roleStr == null || roleStr.isBlank()) {
            role = Role.CUSTOMER;
        } else {
            role = Role.valueOf(roleStr.toUpperCase());
        }


        if (fullName == null || fullName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "fullName is required");
        }

        if (email != null && !email.isBlank()) {
            if (userRepository.existsByEmail(email.trim())) {
                throw new ResponseStatusException(BAD_REQUEST, "email already exists");
            }
        }

         UserEntity user = new UserEntity();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                NOT_FOUND,
                                "User not found with id " + id
                        )
                );
    }

    public UserEntity update(Long id, String fullName, String email, String role) {

        UserEntity user = findById(id);

        if (fullName == null || fullName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "fullName is required");
        }

        user.setFullName(fullName.trim());

        if (email != null && !email.isBlank()) {
            userRepository.findByEmail(email.trim()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new ResponseStatusException(BAD_REQUEST, "email already exists");
                }
            });
            user.setEmail(email.trim());
        }

        if (role != null && !role.isBlank()) {
            user.setRole(Role.valueOf(role.toUpperCase()));
        }

        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.delete(findById(id));
    }
}
