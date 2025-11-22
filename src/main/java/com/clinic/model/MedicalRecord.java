package com.clinic.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime visitDate;
    @Column(columnDefinition = "text")
    private String symptoms;
    @Column(columnDefinition = "text")
    private String diagnosis;
    @Column(columnDefinition = "text")
    private String notes;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionItem> prescriptionItems;
}