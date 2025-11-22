package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.MedicalRecord;
import com.clinic.repo.MedicalRecordRepository;
import com.clinic.service.interfaces.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;

    @Cacheable(value = "allMedicalRecords")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public List<MedicalRecord> getAll() {
        log.info("Fetching all medical records");
        return medicalRecordRepository.findAll();
    }

    @Cacheable(value = "medicalRecords", key = "#id")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public MedicalRecord getById(long id) {
        log.info("Fetching medical record with id: {}", id);
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medical record not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"medicalRecords", "allMedicalRecords"}, allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public MedicalRecord save(MedicalRecord medicalRecord) {
        if (medicalRecord.getId() != null && medicalRecord.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        log.info("Creating new medical record for patient: {}", medicalRecord.getPatient().getId());
        return medicalRecordRepository.save(medicalRecord);
    }

    @Transactional
    @CachePut(value = "medicalRecords", key = "#medicalRecord.id")
    @CacheEvict(value = "allMedicalRecords", allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public MedicalRecord update(MedicalRecord medicalRecord) {
        long id = medicalRecord.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating medical record with id: {}", id);
        MedicalRecord existing = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Medical record not found with id: " + id));

        existing.setVisitDate(medicalRecord.getVisitDate());
        existing.setSymptoms(medicalRecord.getSymptoms());
        existing.setDiagnosis(medicalRecord.getDiagnosis());
        existing.setNotes(medicalRecord.getNotes());
        existing.setPatient(medicalRecord.getPatient());
        existing.setDoctor(medicalRecord.getDoctor());

        return medicalRecordRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = {"medicalRecords", "allMedicalRecords"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting medical record with id: {}", id);
        if (!medicalRecordRepository.existsById(id)) {
            throw new NotFoundException("Medical record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }
}