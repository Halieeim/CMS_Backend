package com.clinic.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;
    private String specialization;
    private String phone;
    private String email;
    private Integer yearsOfExperience;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private ClinicUser user;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;


    @OneToMany(mappedBy = "doctor")
    private List<MedicalRecord> medicalRecords;
}