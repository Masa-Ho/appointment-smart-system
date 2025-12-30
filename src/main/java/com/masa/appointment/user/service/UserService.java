package com.masa.appointment.user.service;

import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.user.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create(String fullName, String email, String role) {
        if (fullName == null || fullName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "fullName is required");
        }

        if (email != null && !email.isBlank()) {
            boolean exists = userRepository.existsByEmail(email);
            if (exists) {
                throw new ResponseStatusException(BAD_REQUEST, "email already exists");
            }
        }

        UserEntity u = new UserEntity();
        u.setFullName(fullName);
        u.setEmail((email == null || email.isBlank()) ? null : email.trim());
        u.setRole((role == null || role.isBlank()) ? "client" : role.trim());

        return userRepository.save(u);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found with id: " + id));
    }

    public UserEntity update(Long id, String fullName, String email, String role) {
        UserEntity existing = findById(id);

        if (fullName == null || fullName.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "fullName is required");
        }

        if (email != null && !email.isBlank()) {
            userRepository.findByEmail(email.trim()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new ResponseStatusException(BAD_REQUEST, "email already exists");
                }
            });
            existing.setEmail(email.trim());
        } else {
            existing.setEmail(null);
        }

        existing.setFullName(fullName.trim());
        existing.setRole((role == null || role.isBlank()) ? existing.getRole() : role.trim());

        return userRepository.save(existing);
    }

    public void delete(Long id) {
        UserEntity existing = findById(id);
        userRepository.delete(existing);
    }
}
