package com.clinic.controller;

import com.clinic.controller.dto.BillDto;
import com.clinic.controller.mapper.BillMapper;
import com.clinic.service.interfaces.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;
    private final BillMapper billMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<BillDto> getBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billMapper.toDto(billService.getById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<BillDto>> getAllBills() {
        return ResponseEntity.ok(billMapper.toDtoList(billService.getAll()));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<BillDto> createBill(@Valid @RequestBody BillDto billDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                billMapper.toDto(billService.save(billMapper.toEntity(billDto)))
        );
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<BillDto> updateBill(@Valid @RequestBody BillDto billDto) {
        return ResponseEntity.ok(billMapper.toDto(billService.update(billMapper.toEntity(billDto))));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}