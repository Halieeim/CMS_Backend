package com.clinic.controller;

import com.clinic.controller.dto.ClinicUserDto;
import com.clinic.controller.mapper.ClinicUserMapper;
import com.clinic.service.interfaces.ClinicUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ClinicUserController {
    private final ClinicUserService userService;
    private final ClinicUserMapper userMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClinicUserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.toDto(userService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClinicUserDto>> getAllUsers() {
        return ResponseEntity.ok(userMapper.toDtoList(userService.getAll()));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClinicUserDto> getCurrentUser() {
        return ResponseEntity.ok(userMapper.toDto(userService.getCurrentUser()));
    }

    @PostMapping("/adduser")
    public ResponseEntity<ClinicUserDto> createUser(@Valid @RequestBody ClinicUserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userMapper.toDto(userService.save(userMapper.toEntity(userDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClinicUserDto> updateUser(@Valid @RequestBody ClinicUserDto userDto) {
        return ResponseEntity.ok(userMapper.toDto(userService.update(userMapper.toEntity(userDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}