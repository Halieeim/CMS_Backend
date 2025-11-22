package com.clinic.service.interfaces;

import com.clinic.controller.dto.SignInDto;
import com.clinic.security.UserDetailsInfo;

public interface AuthenticationService {
    UserDetailsInfo authenticate(SignInDto signInDto);
}
