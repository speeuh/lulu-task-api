package com.luizatasks.config;

import com.luizatasks.domain.entity.User;
import com.luizatasks.domain.enums.ThemeType;
import com.luizatasks.domain.enums.UserRole;
import com.luizatasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.admin.username}")
    private String adminUsername;
    
    @Value("${app.admin.password}")
    private String adminPassword;
    
    @Value("${app.admin.fullname:Administrador}")
    private String adminFullName;
    
    @Value("${app.user.username}")
    private String defaultUsername;
    
    @Value("${app.user.password}")
    private String defaultPassword;
    
    @Value("${app.user.fullname:Lulu}")
    private String defaultFullName;
    
    @Value("${app.init.createdefaultusers:true}")
    private boolean createDefaultUsers;
    
    @Override
    public void run(String... args) {
        if (!createDefaultUsers) {
            System.out.println("Default user creation is disabled");
            return;
        }
        
        // Create admin user if not exists
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .fullName(adminFullName)
                    .role(UserRole.ROLE_ADMIN)
                    .points(0)
                    .themeType(ThemeType.COLOR)
                    .themeValue("#FF9B8A")
                    .active(true)
                    .build();
            userRepository.save(admin);
            System.out.println("Admin user created - username: " + adminUsername);
        }
        
        // Create default user if not exists
        if (!userRepository.existsByUsername(defaultUsername)) {
            User defaultUser = User.builder()
                    .username(defaultUsername)
                    .password(passwordEncoder.encode(defaultPassword))
                    .fullName(defaultFullName)
                    .role(UserRole.ROLE_USER)
                    .points(0)
                    .themeType(ThemeType.COLOR)
                    .themeValue("#FF9B8A")
                    .active(true)
                    .build();
            userRepository.save(defaultUser);
            System.out.println("Default user created - username: " + defaultUsername);
        }
    }
}

