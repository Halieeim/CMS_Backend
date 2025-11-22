package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.PrescriptionItem;
import com.clinic.repo.PrescriptionItemRepository;
import com.clinic.service.interfaces.PrescriptionItemService;
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
public class PrescriptionItemServiceImpl implements PrescriptionItemService {
    private final PrescriptionItemRepository prescriptionItemRepository;

    @Cacheable(value = "allPrescriptionItems")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public List<PrescriptionItem> getAll() {
        log.info("Fetching all prescription items");
        return prescriptionItemRepository.findAll();
    }

    @Cacheable(value = "prescriptionItems", key = "#id")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public PrescriptionItem getById(long id) {
        log.info("Fetching prescription item with id: {}", id);
        return prescriptionItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prescription item not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"prescriptionItems", "allPrescriptionItems"}, allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public PrescriptionItem save(PrescriptionItem prescriptionItem) {
        if (prescriptionItem.getId() != null && prescriptionItem.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        log.info("Creating new prescription item: {}", prescriptionItem.getMedicineName());
        return prescriptionItemRepository.save(prescriptionItem);
    }

    @Transactional
    @CachePut(value = "prescriptionItems", key = "#prescriptionItem.id")
    @CacheEvict(value = "allPrescriptionItems", allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public PrescriptionItem update(PrescriptionItem prescriptionItem) {
        long id = prescriptionItem.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating prescription item with id: {}", id);
        PrescriptionItem existing = prescriptionItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prescription item not found with id: " + id));

        existing.setMedicineName(prescriptionItem.getMedicineName());
        existing.setDosage(prescriptionItem.getDosage());
        existing.setDurationDays(prescriptionItem.getDurationDays());
        existing.setInstructions(prescriptionItem.getInstructions());
        existing.setMedicalRecord(prescriptionItem.getMedicalRecord());

        return prescriptionItemRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = {"prescriptionItems", "allPrescriptionItems"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting prescription item with id: {}", id);
        if (!prescriptionItemRepository.existsById(id)) {
            throw new NotFoundException("Prescription item not found with id: " + id);
        }
        prescriptionItemRepository.deleteById(id);
    }
}