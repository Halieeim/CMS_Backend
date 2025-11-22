package com.clinic.controller;

import com.clinic.controller.dto.AppointmentDto;
import com.clinic.controller.mapper.AppointmentMapper;
import com.clinic.service.interfaces.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentMapper.toDto(appointmentService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentMapper.toDtoList(appointmentService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                appointmentMapper.toDto(appointmentService.save(appointmentMapper.toEntity(appointmentDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<AppointmentDto> updateAppointment(@Valid @RequestBody AppointmentDto appointmentDto) {
        return ResponseEntity.ok(appointmentMapper.toDto(appointmentService.update(appointmentMapper.toEntity(appointmentDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}