package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.Doctor;
import com.clinic.repo.DoctorRepository;
import com.clinic.service.interfaces.DoctorService;
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
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Cacheable(value = "allDoctors")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public List<Doctor> getAll() {
        log.info("Fetching all doctors");
        return doctorRepository.findAll();
    }

    @Cacheable(value = "doctors", key = "#id")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public Doctor getById(long id) {
        log.info("Fetching doctor with id: {}", id);
        return doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"doctors", "allDoctors", "doctorsBySpecialization"}, allEntries = true)
    @Override
    public Doctor save(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("The received object is null.");
        }
        if (doctor.getId() != null && doctor.getId() > 0) {
            throw new IllegalArgumentException("Id must be 0, otherwise you must use update API.");
        }

        log.info("Creating new doctor: {}", doctor.getName());
        return doctorRepository.save(doctor);
    }

    @Transactional
    @CachePut(value = "doctors", key = "#id")
    @CacheEvict(value = {"allDoctors", "doctorsBySpecialization"}, allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public Doctor update(Doctor doctor) {
        long id = doctor.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating doctor with id: {}", doctor.getId());
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Doctor not found with id: " + id));

        existingDoctor.setName(doctor.getName());
        existingDoctor.setSpecialization(doctor.getSpecialization());
        existingDoctor.setPhone(doctor.getPhone());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setYearsOfExperience(doctor.getYearsOfExperience());

        return doctorRepository.save(existingDoctor);
    }

    @Transactional
    @CacheEvict(value = {"doctors", "allDoctors", "doctorsBySpecialization"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting doctor with id: {}", id);
        if (!doctorRepository.existsById(id)) {
            throw new NotFoundException("Doctor not found with id: " + id);
        }
        doctorRepository.deleteById(id);
    }

    @Cacheable(value = "doctorsBySpecialization", key = "#specialization")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        log.info("Fetching doctors by specialization: {}", specialization);
        return doctorRepository.findBySpecialization(specialization);
    }

    @Cacheable(value = "doctorsByExperience", key = "#minYears")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    @Override
    public List<Doctor> getDoctorsByMinimumExperience(Integer minYears) {
        log.info("Fetching doctors with minimum {} years of experience", minYears);
        return doctorRepository.findByYearsOfExperienceGreaterThanEqual(minYears);
    }
}