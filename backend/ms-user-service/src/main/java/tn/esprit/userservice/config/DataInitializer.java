package tn.esprit.userservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.esprit.userservice.entity.AccountStatus;
import tn.esprit.userservice.entity.KycStatus;
import tn.esprit.userservice.entity.Role;
import tn.esprit.userservice.entity.User;
import tn.esprit.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin.email:admin@trustedwork.com}")
    private String adminEmail;

    /**
     * À fournir via une variable d'environnement (recommandé) :
     * - APP_BOOTSTRAP_ADMIN_PASSWORD
     * ou via application.properties :
     * - app.bootstrap.admin.password
     */
    @Value("${app.bootstrap.admin.password:}")
    private String adminPassword;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {

            if (userRepository.existsByEmail(adminEmail)) {
                log.info("Admin already exists");
                return;
            }

            // Fallback DEV uniquement. En production, fournissez app.bootstrap.admin.password / env var.
            String rawPassword = (adminPassword == null || adminPassword.isBlank())
                    ? "ChangeMe_" + System.currentTimeMillis()
                    : adminPassword;

            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("System");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(rawPassword));
            admin.setPhone("00000000");

            admin.setRoles(Set.of(Role.ADMIN));

            admin.setAccountStatus(AccountStatus.ACTIVE);
            admin.setKycStatus(KycStatus.APPROVED);
            admin.setEnabled(true);
            admin.setAccountNonLocked(true);
            admin.setTwoFactorEnabled(false);

            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            userRepository.save(admin);

            if (adminPassword == null || adminPassword.isBlank()) {
                log.warn("Default admin created: {} (mot de passe généré automatiquement car app.bootstrap.admin.password est vide)", adminEmail);
            } else {
                log.info("Default admin created: {}", adminEmail);
            }
        };
    }
}