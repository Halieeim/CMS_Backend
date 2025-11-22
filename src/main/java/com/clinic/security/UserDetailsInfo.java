package com.clinic.security;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsInfo {
    private Long userId;
    private String userName;
    private String roleCode;
    private List<GrantedAuthority> authorities;
    private String token;
}
