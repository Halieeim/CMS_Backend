package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.Bill;
import com.clinic.repo.BillRepository;
import com.clinic.service.interfaces.BillService;
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
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;

    @Cacheable(value = "allBills")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public List<Bill> getAll() {
        log.info("Fetching all bills");
        return billRepository.findAll();
    }

    @Cacheable(value = "bills", key = "#id")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public Bill getById(long id) {
        log.info("Fetching bill with id: {}", id);
        return billRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bill not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"bills", "allBills"}, allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public Bill save(Bill bill) {
        if (bill.getId() != null && bill.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        log.info("Creating new bill for patient: {}", bill.getPatient().getId());
        return billRepository.save(bill);
    }

    @Transactional
    @CachePut(value = "bills", key = "#bill.id")
    @CacheEvict(value = "allBills", allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public Bill update(Bill bill) {
        long id = bill.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating bill with id: {}", id);
        Bill existing = billRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bill not found with id: " + id));

        existing.setBillDate(bill.getBillDate());
        existing.setAmount(bill.getAmount());
        existing.setDiscount(bill.getDiscount());
        existing.setFinalAmount(bill.getFinalAmount());
        existing.setPaymentStatus(bill.getPaymentStatus());
        existing.setPaymentMethod(bill.getPaymentMethod());
        existing.setPatient(bill.getPatient());
        existing.setAppointment(bill.getAppointment());
        existing.setNotes(bill.getNotes());

        return billRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = {"bills", "allBills"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting bill with id: {}", id);
        if (!billRepository.existsById(id)) {
            throw new NotFoundException("Bill not found with id: " + id);
        }
        billRepository.deleteById(id);
    }
}