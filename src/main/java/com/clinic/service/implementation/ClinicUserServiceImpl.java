package com.clinic.service.implementation;

import com.clinic.controller.dto.SignInDto;
import com.clinic.exception.NotAuthenticatedException;
import com.clinic.exception.NotFoundException;
import com.clinic.model.Authority;
import com.clinic.model.ClinicUser;
import com.clinic.model.Role;
import com.clinic.repo.ClinicUserRepository;
import com.clinic.security.UserDetailsImpl;
import com.clinic.service.interfaces.ClinicUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClinicUserServiceImpl implements ClinicUserService {
    private final ClinicUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "allUsers")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<ClinicUser> getAll() {
        return userRepository.findAll();
    }

    @Cacheable(value = "users", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ClinicUser getById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"users", "allUsers"}, allEntries = true)
    @Override
    public ClinicUser save(ClinicUser user) {
        if (user.getId() != null && user.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @CachePut(value = "users", key = "#user.id")
    @CacheEvict(value = "allUsers", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ClinicUser update(ClinicUser user) {
        long id = user.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        ClinicUser existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        existing.setUsername(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existing.setRoles(user.getRoles());

        return userRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = {"users", "allUsers"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).map(user -> {
            Set<Role> userRoles = user.getRoles();

            Set<String> authorities = userRoles.stream().flatMap(r -> r.getAuthorities().stream())
                    .map(Authority::getAuthority).collect(Collectors.toSet());

            authorities.addAll(userRoles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));

            String[] rolesAndAuthorities = authorities.toArray(new String[0]);

            return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                    .password(user.getPassword()).authorities(rolesAndAuthorities).build();
        }).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

    }

    @Override
    public boolean usernameAlreadyExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public UserDetailsImpl loadUserByUsername(SignInDto signInDto) throws UsernameNotFoundException {
        return userRepository.findByUsername(signInDto.getUsername())
                .map(user -> {
                    // Retrieve user roles and authorities
                    Set<Role> userRoles = user.getRoles();

                    // Get all authorities from roles
                    List<GrantedAuthority> authorities = userRoles.stream()
                            .flatMap(role -> role.getAuthorities().stream())
                            .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                            .collect(Collectors.toList());

                    // Build and return UserDetailsImpl
                    return UserDetailsImpl.builder()
                            .user(user)
                            .authorities(authorities)
                            .roleCode(userRoles.toString())  // Or however you want to set this
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
    }

    @Override
    public ClinicUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("The Authenticated User is not found in database"));
        }
        throw new NotAuthenticatedException("No user is currently authenticated");
    }

    @Override
    public boolean isCurrentUser(ClinicUser user) {
        return getCurrentUser().getId().equals(user.getId());
    }
}
