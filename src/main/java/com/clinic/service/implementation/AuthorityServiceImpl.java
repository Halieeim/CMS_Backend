package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.Authority;
import com.clinic.repo.AuthorityRepository;
import com.clinic.service.interfaces.AuthorityService;
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
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository authorityRepository;

    @Cacheable(value = "allAuthorities")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<Authority> getAll() {
        log.info("Fetching all authorities");
        return authorityRepository.findAll();
    }

    @Cacheable(value = "authorities", key = "#id")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Authority getById(long id) {
        log.info("Fetching authority with id: {}", id);
        return authorityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Authority not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"authorities", "allAuthorities"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Authority save(Authority authority) {
        if (authority.getId() != null && authority.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        log.info("Creating new authority: {}", authority.getName());
        return authorityRepository.save(authority);
    }

    @Transactional
    @CachePut(value = "authorities", key = "#authority.id")
    @CacheEvict(value = "allAuthorities", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Authority update(Authority authority) {
        long id = authority.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating authority with id: {}", id);
        Authority existing = authorityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Authority not found with id: " + id));

        existing.setName(authority.getName());

        return authorityRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = {"authorities", "allAuthorities"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting authority with id: {}", id);
        if (!authorityRepository.existsById(id)) {
            throw new NotFoundException("Authority not found with id: " + id);
        }
        authorityRepository.deleteById(id);
    }
}