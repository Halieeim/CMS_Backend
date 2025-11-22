package com.clinic.controller;

import com.clinic.controller.dto.RoleDto;
import com.clinic.controller.mapper.RoleMapper;
import com.clinic.service.interfaces.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleMapper.toDto(roleService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        return ResponseEntity.ok(roleMapper.toDtoList(roleService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleDto roleDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                roleMapper.toDto(roleService.save(roleMapper.toEntity(roleDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> updateRole(@Valid @RequestBody RoleDto roleDto) {
        return ResponseEntity.ok(roleMapper.toDto(roleService.update(roleMapper.toEntity(roleDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}