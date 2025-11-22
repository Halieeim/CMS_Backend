package com.clinic.controller.mapper;

import com.clinic.controller.dto.ClinicUserDto;
import com.clinic.model.ClinicUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = ClinicUserMapperUtils.class)
public interface ClinicUserMapper {
    @Mapping(source = "roles", target = "roleIds", qualifiedByName = "mapRolesToRoleIds")
    ClinicUserDto toDto(ClinicUser user);

    @Mapping(source = "roleIds", target = "roles", qualifiedByName = "mapRoleIdsToRoles")
    ClinicUser toEntity(ClinicUserDto dto);

    List<ClinicUserDto> toDtoList(List<ClinicUser> users);
    List<ClinicUser> toEntityList(List<ClinicUserDto> dtos);
}