package com.clinic.controller.mapper;

import com.clinic.controller.dto.AppointmentDto;
import com.clinic.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AppointmentMapperUtils.class)
public interface AppointmentMapper {
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "bill.id", target = "billId")
    AppointmentDto toDto(Appointment appointment);

    @Mapping(source = "patientId", target = "patient", qualifiedByName = "mapPatientIdToPatient")
    @Mapping(source = "doctorId", target = "doctor", qualifiedByName = "mapDoctorIdToDoctor")
    @Mapping(source = "billId", target = "bill", qualifiedByName = "mapBillIdToBill")
    Appointment toEntity(AppointmentDto dto);

    List<AppointmentDto> toDtoList(List<Appointment> appointments);
    List<Appointment> toEntityList(List<AppointmentDto> dtos);
}