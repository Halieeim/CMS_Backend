package com.clinic.controller.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private List<Long> appointmentIds;
    private List<Long> medicalRecordIds;
    private List<Long> billIds;
}