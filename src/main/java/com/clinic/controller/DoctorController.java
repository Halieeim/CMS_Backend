package com.clinic.controller;

import com.clinic.controller.dto.DoctorDto;
import com.clinic.controller.mapper.DoctorMapper;
import com.clinic.service.implementation.DoctorServiceImpl;
import com.clinic.service.interfaces.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorMapper.toDto(doctorService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorMapper.toDtoList(doctorService.getAll()));
    }

    @GetMapping("/specialization/{specialization}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialization(
            @PathVariable String specialization) {
        return ResponseEntity.ok(doctorMapper.toDtoList(doctorService.getDoctorsBySpecialization(specialization)));
    }

    @GetMapping("/experience/{minYears}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<List<DoctorDto>> getDoctorsByMinimumExperience(
            @PathVariable Integer minYears) {
        return ResponseEntity.ok(doctorMapper.toDtoList(doctorService.getDoctorsByMinimumExperience(minYears)));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> createDoctor(@Valid @RequestBody DoctorDto doctorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                doctorMapper.toDto(doctorService.save(doctorMapper.toEntity(doctorDTO)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DoctorDto> updateDoctor(@Valid @RequestBody DoctorDto doctorDTO) {
        return ResponseEntity.ok(doctorMapper.toDto(doctorService.update(doctorMapper.toEntity(doctorDTO))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}