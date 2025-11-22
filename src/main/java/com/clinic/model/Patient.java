package com.clinic.model;

import com.clinic.model.constants.Gender;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;
    private String lastName;


    @Enumerated(EnumType.STRING)
    private Gender gender;


    private LocalDate dateOfBirth;
    private String phone;
    private String email;


    @Column(length = 500)
    private String address;


    private String emergencyContactName;
    private String emergencyContactPhone;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private ClinicUser user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecord> medicalRecords;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bill> bills;
}