package com.clinic.controller.mapper;

import com.clinic.controller.dto.PatientDto;
import com.clinic.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = PatientMapperUtils.class)
public interface PatientMapper {
    @Mapping(source = "appointments", target = "appointmentIds", qualifiedByName = "mapAppointmentsToIds")
    @Mapping(source = "medicalRecords", target = "medicalRecordIds", qualifiedByName = "mapMedicalRecordsToIds")
    @Mapping(source = "bills", target = "billIds", qualifiedByName = "mapBillsToIds")
    PatientDto toDto(Patient patient);

    @Mapping(source = "appointmentIds", target = "appointments", qualifiedByName = "mapIdsToAppointments")
    @Mapping(source = "medicalRecordIds", target = "medicalRecords", qualifiedByName = "mapIdsToMedicalRecords")
    @Mapping(source = "billIds", target = "bills", qualifiedByName = "mapIdsToBills")
    Patient toEntity(PatientDto dto);

    List<PatientDto> toDtoList(List<Patient> patients);
    List<Patient> toEntityList(List<PatientDto> dtos);
}