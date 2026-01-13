package com.masa.appointment.user.controller;

import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public static class UpsertUserRequest {
        @NotBlank public String fullName;
        @Email public String email;  
        @NotBlank public String password; 
        public String role; 
            
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserEntity create(@Valid @RequestBody UpsertUserRequest req) {
        return userService.create(req.fullName, req.email, req.password, req.role);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping
    public List<UserEntity> list() {
        return userService.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/{id}")
    public UserEntity get(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserEntity update(@PathVariable Long id, @Valid @RequestBody UpsertUserRequest req) {
        return userService.update(id, req.fullName, req.email, req.role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
