package com.clinic.controller.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicUserDto {
    private Long id;
    private String username;
    private String password;
    private Set<Long> roleIds;
}
