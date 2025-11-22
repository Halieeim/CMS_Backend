package com.clinic.controller.dto;

import com.clinic.model.constants.PaymentMethod;
import com.clinic.model.constants.PaymentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDto {
    private Long id;
    private LocalDate billDate;
    private Double amount;
    private Double discount;
    private Double finalAmount;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private Long patientId;
    private Long appointmentId;
    private String notes;
}
