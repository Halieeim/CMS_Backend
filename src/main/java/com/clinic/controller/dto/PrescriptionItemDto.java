package com.clinic.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemDto {
    private Long id;
    private String medicineName;
    private String dosage;
    private int durationDays;
    private String instructions;
    private Long medicalRecordId;
}
