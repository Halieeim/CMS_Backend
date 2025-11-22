package com.clinic;

import com.clinic.model.Authority;
import com.clinic.model.Role;
import com.clinic.repo.AuthorityRepository;
import com.clinic.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initializing database with default roles and authorities...");

        // Create Authorities
        Authority adminAuth = createAuthority("ROLE_ADMIN");
        Authority doctorAuth = createAuthority("ROLE_DOCTOR");
        Authority patientAuth = createAuthority("ROLE_PATIENT");

        // Create Roles
        Role adminRole = createRole("ADMIN");
        Role doctorRole = createRole("DOCTOR");
        Role patientRole = createRole("PATIENT");

        // Assign authorities to roles
        // ADMIN has all authorities
        adminRole.addAuthority(adminAuth, doctorAuth, patientAuth);

        // DOCTOR has DOCTOR authority
        doctorRole.addAuthority(doctorAuth);

        // PATIENT has PATIENT authority
        patientRole.addAuthority(patientAuth);

        // Save roles with authorities
        roleRepository.save(adminRole);
        roleRepository.save(doctorRole);
        roleRepository.save(patientRole);

        log.info("Database initialization completed successfully!");
    }

    private Authority createAuthority(String name) {
        if (authorityRepository.findByName(name).isEmpty()) {
            Authority authority = new Authority();
            authority.setName(name);
            return authorityRepository.save(authority);
        }
        return authorityRepository.findByName(name).get();
    }

    private Role createRole(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            return role;
        }
        return roleRepository.findByName(name).get();
    }
}