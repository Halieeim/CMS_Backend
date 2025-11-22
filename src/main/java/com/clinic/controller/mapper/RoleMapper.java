package com.clinic.controller.mapper;

import com.clinic.controller.dto.RoleDto;
import com.clinic.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = RoleMapperUtils.class)
public interface RoleMapper {
    @Mapping(source = "authorities", target = "authorityIds", qualifiedByName = "mapAuthoritiesToIds")
    RoleDto toDto(Role role);

    @Mapping(source = "authorityIds", target = "authorities", qualifiedByName = "mapIdsToAuthorities")
    Role toEntity(RoleDto dto);

    List<RoleDto> toDtoList(List<Role> roles);
    List<Role> toEntityList(List<RoleDto> dtos);
}