package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.Patient;
import com.clinic.repo.PatientRepository;
import com.clinic.service.interfaces.PatientService;
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
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;

    @Cacheable(value = "allPatients")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public List<Patient> getAll() {
        log.info("Fetching all patients");
        return patientRepository.findAll();
    }

    @Cacheable(value = "patients", key = "#id")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public Patient getById(long id) {
        log.info("Fetching patient with id: {}", id);
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"patients", "allPatients"}, allEntries = true)
    @Override
    public Patient save(Patient patient) {
        if (patient.getId() != null && patient.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        log.info("Creating new patient: {} {}", patient.getFirstName(), patient.getLastName());
        return patientRepository.save(patient);
    }

    @Transactional
    @CachePut(value = "patients", key = "#patient.id")
    @CacheEvict(value = "allPatients", allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    @Override
    public Patient update(Patient patient) {
        long id = patient.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating patient with id: {}", id);
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found with id: " + id));

        existingPatient.setFirstName(patient.getFirstName());
        existingPatient.setLastName(patient.getLastName());
        existingPatient.setGender(patient.getGender());
        existingPatient.setDateOfBirth(patient.getDateOfBirth());
        existingPatient.setPhone(patient.getPhone());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setEmergencyContactName(patient.getEmergencyContactName());
        existingPatient.setEmergencyContactPhone(patient.getEmergencyContactPhone());

        return patientRepository.save(existingPatient);
    }

    @Transactional
    @CacheEvict(value = {"patients", "allPatients"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting patient with id: {}", id);
        if (!patientRepository.existsById(id)) {
            throw new NotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}