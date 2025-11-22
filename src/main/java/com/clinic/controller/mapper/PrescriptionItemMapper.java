package com.clinic.controller.mapper;

import com.clinic.controller.dto.PrescriptionItemDto;
import com.clinic.model.PrescriptionItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = PrescriptionItemMapperUtils.class)
public interface PrescriptionItemMapper {
    @Mapping(source = "medicalRecord.id", target = "medicalRecordId")
    PrescriptionItemDto toDto(PrescriptionItem prescriptionItem);

    @Mapping(source = "medicalRecordId", target = "medicalRecord", qualifiedByName = "mapMedicalRecordIdToMedicalRecord")
    PrescriptionItem toEntity(PrescriptionItemDto dto);

    List<PrescriptionItemDto> toDtoList(List<PrescriptionItem> prescriptionItems);
    List<PrescriptionItem> toEntityList(List<PrescriptionItemDto> dtos);
}