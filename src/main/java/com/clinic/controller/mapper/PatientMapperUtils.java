package com.clinic.controller.mapper;

import com.clinic.model.Appointment;
import com.clinic.model.Bill;
import com.clinic.model.MedicalRecord;
import com.clinic.repo.AppointmentRepository;
import com.clinic.repo.BillRepository;
import com.clinic.repo.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PatientMapperUtils {
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final BillRepository billRepository;

    @Named("mapIdsToAppointments")
    public List<Appointment> mapIdsToAppointments(List<Long> appointmentIds) {
        if (appointmentIds == null) return List.of();
        return appointmentRepository.findByIdIn(appointmentIds);
    }

    @Named("mapAppointmentsToIds")
    public List<Long> mapAppointmentsToIds(List<Appointment> appointments) {
        if (appointments == null) return List.of();
        return appointments.stream().map(Appointment::getId).toList();
    }

    @Named("mapIdsToMedicalRecords")
    public List<MedicalRecord> mapIdsToMedicalRecords(List<Long> medicalRecordIds) {
        if (medicalRecordIds == null) return List.of();
        return medicalRecordRepository.findByIdIn(medicalRecordIds);
    }

    @Named("mapMedicalRecordsToIds")
    public List<Long> mapMedicalRecordsToIds(List<MedicalRecord> medicalRecords) {
        if (medicalRecords == null) return List.of();
        return medicalRecords.stream().map(MedicalRecord::getId).toList();
    }

    @Named("mapIdsToBills")
    public List<Bill> mapIdsToBills(List<Long> billIds) {
        if (billIds == null) return List.of();
        return billRepository.findAllById(billIds);
    }

    @Named("mapBillsToIds")
    public List<Long> mapBillsToIds(List<Bill> bills) {
        if (bills == null) return List.of();
        return bills.stream().map(Bill::getId).toList();
    }
}