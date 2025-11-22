package com.clinic.service.implementation;

import com.clinic.exception.NotFoundException;
import com.clinic.model.Appointment;
import com.clinic.repo.AppointmentRepository;
import com.clinic.service.interfaces.AppointmentService;
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
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Cacheable(value = "allAppointments")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    @Override
    public List<Appointment> getAll() {
        log.info("Fetching all appointments");
        return appointmentRepository.findAll();
    }

    @Cacheable(value = "appointments", key = "#id")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public Appointment getById(long id) {
        log.info("Fetching appointment with id: {}", id);
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = {"appointments", "allAppointments"}, allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getId() != null && appointment.getId() > 0) {
            throw new IllegalArgumentException("Id must be null or 0, otherwise use update API");
        }
        log.info("Creating new appointment for patient: {}", appointment.getPatient().getId());
        return appointmentRepository.save(appointment);
    }

    @Transactional
    @CachePut(value = "appointments", key = "#appointment.id")
    @CacheEvict(value = "allAppointments", allEntries = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    @Override
    public Appointment update(Appointment appointment) {
        long id = appointment.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Id is 0, you must use Save API not Update API");
        }
        log.info("Updating appointment with id: {}", id);
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found with id: " + id));

        existing.setAppointmentDate(appointment.getAppointmentDate());
        existing.setAppointmentTime(appointment.getAppointmentTime());
        existing.setStatus(appointment.getStatus());
        existing.setPaid(appointment.isPaid());
        existing.setReason(appointment.getReason());
        existing.setPatient(appointment.getPatient());
        existing.setDoctor(appointment.getDoctor());

        return appointmentRepository.save(existing);
    }

    @Transactional
    @CacheEvict(value = {"appointments", "allAppointments"}, allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteById(long id) {
        log.info("Deleting appointment with id: {}", id);
        if (!appointmentRepository.existsById(id)) {
            throw new NotFoundException("Appointment not found with id: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}