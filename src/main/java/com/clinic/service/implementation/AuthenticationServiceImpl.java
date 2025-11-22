package com.clinic.service.implementation;

import com.clinic.controller.dto.SignInDto;
import com.clinic.exception.NotAuthenticatedException;
import com.clinic.security.JwtUtil;
import com.clinic.security.UserDetailsImpl;
import com.clinic.security.UserDetailsInfo;
import com.clinic.service.interfaces.AuthenticationService;
import com.clinic.service.interfaces.ClinicUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final ClinicUserService userService ;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetailsInfo authenticate(SignInDto signInDto) {
        if(signInDto == null) throw new IllegalArgumentException("Credentials are not present!!!");
        Authentication authentication;
        try {
            authentication =  authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.getUsername(),signInDto.getPassword())
            );

        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new NotAuthenticatedException("INVALID_CREDENTIALS");
        }


        UserDetailsImpl userInfo = userService.loadUserByUsername(signInDto);
        String token = jwtUtil.generateToken(userInfo);


        UserDetailsInfo userDetailsInfo = UserDetailsInfo.builder().
                userName(userInfo.getUsername()).userId(userInfo.getUser().getId())
                .authorities((List<GrantedAuthority>) userInfo.getAuthorities())
                .roleCode(userInfo.getRoleCode())
                .token(token)
                .build();

        return userDetailsInfo;
    }
}
