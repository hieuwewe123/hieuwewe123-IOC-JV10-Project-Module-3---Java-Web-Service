package org.example.coursemanagementsystem.config;

import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.entity.UserRole;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Seed admin một lần, không xóa dữ liệu hiện có.
        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = User.builder()
                .username("admin")
                .email("admin@gmail.com")
                .passwordHash(passwordEncoder.encode("admin123"))   // ← Phải encode bằng PasswordEncoder
                .fullName("System Administrator")
                .role(UserRole.ADMIN)
                .isActive(true)
                .build();

        userRepository.save(admin);

        System.out.println("=====================================");
        System.out.println("✅ ĐÃ TẠO USER ADMIN THÀNH CÔNG!");
        System.out.println("Username : admin");
        System.out.println("Password : admin123");
        System.out.println("Role     : ADMIN");
        System.out.println("=====================================");
    }
}