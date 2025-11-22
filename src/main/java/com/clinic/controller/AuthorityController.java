package com.clinic.controller;

import com.clinic.controller.dto.AuthorityDto;
import com.clinic.controller.mapper.AuthorityMapper;
import com.clinic.service.interfaces.AuthorityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authorities")
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;
    private final AuthorityMapper authorityMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorityDto> getAuthorityById(@PathVariable Long id) {
        return ResponseEntity.ok(authorityMapper.toDto(authorityService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuthorityDto>> getAllAuthorities() {
        return ResponseEntity.ok(authorityMapper.toDtoList(authorityService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorityDto> createAuthority(@Valid @RequestBody AuthorityDto authorityDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                authorityMapper.toDto(authorityService.save(authorityMapper.toEntity(authorityDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorityDto> updateAuthority(@Valid @RequestBody AuthorityDto authorityDto) {
        return ResponseEntity.ok(authorityMapper.toDto(authorityService.update(authorityMapper.toEntity(authorityDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuthority(@PathVariable Long id) {
        authorityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}