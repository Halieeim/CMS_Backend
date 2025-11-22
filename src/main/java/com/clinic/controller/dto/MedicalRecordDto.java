package com.clinic.controller.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordDto {
    private Long id;
    private LocalDateTime visitDate;
    private String symptoms;
    private String diagnosis;
    private String notes;
    private Long patientId;
    private Long doctorId;
    private List<PrescriptionItemDto> prescriptionItems;
}
