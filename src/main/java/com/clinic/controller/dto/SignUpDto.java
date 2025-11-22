package com.clinic.controller.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private Long id;
    private Boolean isDoctor;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be between 10 and 11 digits") // 10 for landline
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String specialization;
    private String gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;

    @Min(value = 0, message = "Years of experience must be at least 0")
    @Max(value = 70, message = "Years of experience cannot exceed 70")
    private Integer yearsOfExperience;
    private LocalDate dateOfBirth;
}
