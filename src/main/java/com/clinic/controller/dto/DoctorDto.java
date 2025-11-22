package com.clinic.controller.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto implements Serializable {

    private Long id;
    private Long userId;

    private String name;

    @NotBlank(message = "Specialization is required")
    @Size(min = 2, max = 100, message = "Specialization must be between 2 and 100 characters")
    private String specialization;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be between 10 and 11 digits") // 10 for landline
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience must be at least 0")
    @Max(value = 70, message = "Years of experience cannot exceed 70")
    private Integer yearsOfExperience;

    private List<Long> appointmentIds;
    private List<Long> medicalRecordIds;
}