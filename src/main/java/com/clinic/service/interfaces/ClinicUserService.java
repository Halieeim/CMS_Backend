package com.clinic.service.interfaces;

import com.clinic.controller.dto.SignInDto;
import com.clinic.model.ClinicUser;
import com.clinic.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ClinicUserService extends BaseService<ClinicUser>, UserDetailsService {
    UserDetailsImpl loadUserByUsername(SignInDto signInDto) throws UsernameNotFoundException;
    ClinicUser getCurrentUser();
    boolean isCurrentUser(ClinicUser user);
    boolean usernameAlreadyExists(String username);
}
