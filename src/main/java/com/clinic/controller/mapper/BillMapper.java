package com.clinic.controller.mapper;

import com.clinic.controller.dto.BillDto;
import com.clinic.model.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = BillMapperUtils.class)
public interface BillMapper {
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "appointment.id", target = "appointmentId")
    BillDto toDto(Bill bill);

    @Mapping(source = "patientId", target = "patient", qualifiedByName = "mapPatientIdToPatient")
    @Mapping(source = "appointmentId", target = "appointment", qualifiedByName = "mapAppointmentIdToAppointment")
    Bill toEntity(BillDto dto);

    List<BillDto> toDtoList(List<Bill> bills);
    List<Bill> toEntityList(List<BillDto> dtos);
}