package com.clinic.controller.mapper;

import com.clinic.model.Doctor;
import com.clinic.model.Patient;
import com.clinic.repo.DoctorRepository;
import com.clinic.repo.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicalRecordMapperUtils {
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Named("mapPatientIdToPatient")
    public Patient mapPatientIdToPatient(Long patientId) {
        return patientId != null ? patientRepository.findById(patientId).orElse(null) : null;
    }

    @Named("mapDoctorIdToDoctor")
    public Doctor mapDoctorIdToDoctor(Long doctorId) {
        return doctorId != null ? doctorRepository.findById(doctorId).orElse(null) : null;
    }
}