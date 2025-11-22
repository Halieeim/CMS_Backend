package com.clinic.controller.mapper;

import com.clinic.controller.dto.DoctorDto;
import com.clinic.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = DoctorMapperUtils.class)
public interface DoctorMapper {
    @Mapping(source = "appointments", target = "appointmentIds", qualifiedByName = "mapAppointmentsToAppointmentIds")
    @Mapping(source = "medicalRecords", target = "medicalRecordIds", qualifiedByName = "mapMedicalRecordsToMedicalRecordIds")
    DoctorDto toDto(Doctor doctor);

    @Mapping(source = "appointmentIds", target = "appointments", qualifiedByName = "mapAppointmentIdsToAppointments")
    @Mapping(source = "medicalRecordIds", target = "medicalRecords", qualifiedByName = "mapMedicalRecordIdsToMedicalRecords")
    Doctor toEntity(DoctorDto dto);

    List<DoctorDto> toDtoList(List<Doctor> doctors);

    List<Doctor> toEntityList(List<DoctorDto> dtos);
}
