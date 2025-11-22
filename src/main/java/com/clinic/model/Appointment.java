package com.clinic.model;

import com.clinic.model.constants.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate appointmentDate;
    private LocalTime appointmentTime;


    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;


    private boolean paid;
    private String reason;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Bill bill;
}