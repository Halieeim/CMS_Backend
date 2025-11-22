package com.clinic.controller.mapper;

import com.clinic.model.Appointment;
import com.clinic.model.MedicalRecord;
import com.clinic.repo.AppointmentRepository;
import com.clinic.repo.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorMapperUtils {
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Named("mapAppointmentIdsToAppointments")
    public List<Appointment> mapAppointmentIdsToAppointments(List<Long> appointmentIds){
        return appointmentRepository.findByIdIn(appointmentIds);
    }

    @Named("mapAppointmentsToAppointmentIds")
    public List<Long> mapAppointmentsToAppointmentIds(List<Appointment> appointments){
        return appointments.stream().map(Appointment::getId).toList();
    }

    @Named("mapMedicalRecordIdsToMedicalRecords")
    public List<MedicalRecord> mapMedicalRecordIdsToMedicalRecords(List<Long> MedicalRecordIds){
        return medicalRecordRepository.findByIdIn(MedicalRecordIds);
    }

    @Named("mapMedicalRecordsToMedicalRecordIds")
    public List<Long> mapMedicalRecordsToMedicalRecordIds(List<MedicalRecord> appointments){
        return appointments.stream().map(MedicalRecord::getId).toList();
    }
}
