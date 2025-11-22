package com.clinic.controller;

import com.clinic.controller.dto.PatientDto;
import com.clinic.controller.mapper.PatientMapper;
import com.clinic.service.interfaces.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientMapper.toDto(patientService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientMapper.toDtoList(patientService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientDto patientDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                patientMapper.toDto(patientService.save(patientMapper.toEntity(patientDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<PatientDto> updatePatient(@Valid @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(patientMapper.toDto(patientService.update(patientMapper.toEntity(patientDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}