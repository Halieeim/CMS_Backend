package com.clinic.controller;

import com.clinic.controller.dto.PrescriptionItemDto;
import com.clinic.controller.mapper.PrescriptionItemMapper;
import com.clinic.service.interfaces.PrescriptionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescription-items")
@RequiredArgsConstructor
public class PrescriptionItemController {
    private final PrescriptionItemService prescriptionItemService;
    private final PrescriptionItemMapper prescriptionItemMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<PrescriptionItemDto> getPrescriptionItemById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionItemMapper.toDto(prescriptionItemService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PrescriptionItemDto>> getAllPrescriptionItems() {
        return ResponseEntity.ok(prescriptionItemMapper.toDtoList(prescriptionItemService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionItemDto> createPrescriptionItem(@Valid @RequestBody PrescriptionItemDto prescriptionItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                prescriptionItemMapper.toDto(prescriptionItemService.save(prescriptionItemMapper.toEntity(prescriptionItemDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionItemDto> updatePrescriptionItem(@Valid @RequestBody PrescriptionItemDto prescriptionItemDto) {
        return ResponseEntity.ok(prescriptionItemMapper.toDto(prescriptionItemService.update(prescriptionItemMapper.toEntity(prescriptionItemDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePrescriptionItem(@PathVariable Long id) {
        prescriptionItemService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}