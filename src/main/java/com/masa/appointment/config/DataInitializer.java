package com.masa.appointment.config;

import com.masa.appointment.user.entity.Role;
import com.masa.appointment.user.entity.UserEntity;
import com.masa.appointment.user.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.count() == 0) {

                UserEntity admin = new UserEntity();
                admin.setFullName("Rama");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println(" Default ADMIN created: admin@system.com / admin123");
            }
        };
    }
}
