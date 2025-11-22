package com.clinic.controller.mapper;

import com.clinic.controller.dto.MedicalRecordDto;
import com.clinic.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MedicalRecordMapperUtils.class, PrescriptionItemMapper.class})
public interface MedicalRecordMapper {
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "doctor.id", target = "doctorId")
    MedicalRecordDto toDto(MedicalRecord medicalRecord);

    @Mapping(source = "patientId", target = "patient", qualifiedByName = "mapPatientIdToPatient")
    @Mapping(source = "doctorId", target = "doctor", qualifiedByName = "mapDoctorIdToDoctor")
    MedicalRecord toEntity(MedicalRecordDto dto);

    List<MedicalRecordDto> toDtoList(List<MedicalRecord> medicalRecords);
    List<MedicalRecord> toEntityList(List<MedicalRecordDto> dtos);
}