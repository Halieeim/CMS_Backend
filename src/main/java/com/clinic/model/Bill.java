package com.clinic.model;



import com.clinic.model.constants.PaymentMethod;
import com.clinic.model.constants.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate billDate;
    private Double amount;
    private Double discount;
    private Double finalAmount;


    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;


    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    private String notes;
}