package com.clinic.controller;

import com.clinic.controller.dto.MedicalRecordDto;
import com.clinic.controller.mapper.MedicalRecordMapper;
import com.clinic.service.interfaces.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;
    private final MedicalRecordMapper medicalRecordMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<MedicalRecordDto> getMedicalRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordMapper.toDto(medicalRecordService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<MedicalRecordDto>> getAllMedicalRecords() {
        return ResponseEntity.ok(medicalRecordMapper.toDtoList(medicalRecordService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<MedicalRecordDto> createMedicalRecord(@Valid @RequestBody MedicalRecordDto medicalRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                medicalRecordMapper.toDto(medicalRecordService.save(medicalRecordMapper.toEntity(medicalRecordDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<MedicalRecordDto> updateMedicalRecord(@Valid @RequestBody MedicalRecordDto medicalRecordDto) {
        return ResponseEntity.ok(medicalRecordMapper.toDto(medicalRecordService.update(medicalRecordMapper.toEntity(medicalRecordDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}