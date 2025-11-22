package com.clinic.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityDto {
    private Long id;
    private String name;
}