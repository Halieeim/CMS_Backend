package com.clinic.controller.mapper;

import com.clinic.model.MedicalRecord;
import com.clinic.repo.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrescriptionItemMapperUtils {
    private final MedicalRecordRepository medicalRecordRepository;

    @Named("mapMedicalRecordIdToMedicalRecord")
    public MedicalRecord mapMedicalRecordIdToMedicalRecord(Long medicalRecordId) {
        return medicalRecordId != null ? medicalRecordRepository.findById(medicalRecordId).orElse(null) : null;
    }
}