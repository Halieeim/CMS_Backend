package com.clinic.controller.dto;

import com.clinic.model.constants.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private AppointmentStatus status;
    private boolean paid;
    private String reason;
    private Long patientId;
    private Long doctorId;
    private Long billId;
}
