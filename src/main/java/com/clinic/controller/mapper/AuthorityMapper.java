package com.clinic.controller.mapper;

import com.clinic.controller.dto.AuthorityDto;
import com.clinic.model.Authority;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {
    AuthorityDto toDto(Authority authority);
    Authority toEntity(AuthorityDto dto);
    List<AuthorityDto> toDtoList(List<Authority> authorities);
    List<Authority> toEntityList(List<AuthorityDto> dtos);
}