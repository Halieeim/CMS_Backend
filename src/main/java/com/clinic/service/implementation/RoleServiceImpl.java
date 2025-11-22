package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.Role;
import com.clinic.repo.RoleRepository;
import com.clinic.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Cacheable(value = "allRoles")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<Role> getAll() {
        log.info("Fetching all roles");
        return roleRepository.findAll();
    }

    @Cacheable(value = "roles", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Role getById(long id) {
        log.info("Fetching role with id: {}", id);
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"roles", "allRoles"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Role save(Role role) {
        if (role.getId() != null && role.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        log.info("Creating new role: {}", role.getName());
        return roleRepository.save(role);
    }

    @Transactional
    @CachePut(value = "roles", key = "#role.id")
    @CacheEvict(value = "allRoles", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Role update(Role role) {
        long id = role.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating role with id: {}", id);
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found with id: " + id));

        existing.setName(role.getName());
        existing.setAuthorities(role.getAuthorities());

        return roleRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = {"roles", "allRoles"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting role with id: {}", id);
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}