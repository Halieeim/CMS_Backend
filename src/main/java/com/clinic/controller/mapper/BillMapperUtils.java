package com.clinic.controller.mapper;

import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import com.clinic.repo.AppointmentRepository;
import com.clinic.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillMapperUtils {
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Named("mapPatientIdToPatient")
    public Patient mapPatientIdToPatient(Long patientId) {
        return patientId != null ? patientRepository.findById(patientId).orElse(null) : null;
    }

    @Named("mapAppointmentIdToAppointment")
    public Appointment mapAppointmentIdToAppointment(Long appointmentId) {
        return appointmentId != null ? appointmentRepository.findById(appointmentId).orElse(null) : null;
    }
}