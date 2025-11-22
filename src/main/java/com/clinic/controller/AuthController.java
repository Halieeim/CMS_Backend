package com.clinic.controller;

import com.clinic.controller.dto.*;
import com.clinic.controller.mapper.ClinicUserMapper;
import com.clinic.controller.mapper.DoctorMapper;
import com.clinic.controller.mapper.PatientMapper;
import com.clinic.exception.NotAuthenticatedException;
import com.clinic.model.ClinicUser;
import com.clinic.security.UserDetailsInfo;
import com.clinic.service.interfaces.AuthenticationService;
import com.clinic.service.interfaces.ClinicUserService;
import com.clinic.service.interfaces.DoctorService;
import com.clinic.service.interfaces.PatientService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final ClinicUserMapper userMapper;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final ClinicUserService userService ;
    private final DoctorService doctorService;
    private final PatientService patientService;

    private static final long DOCTOR_TYPE_ID = 2;
    private static final long PATIENT_TYPE_ID = 3;

    @PostMapping(value = "/authenticate")
    public UserDetailsInfo signIn(@RequestBody SignInDto signInDto) throws NotAuthenticatedException {
        return authenticationService.authenticate(signInDto);
    }

    @PostMapping(value = "/signup")
    @Transactional(rollbackFor = Exception.class)
    public UserDetailsInfo signUp(@RequestBody SignUpDto signUpDto) {
        // check if username already exists
        if (userService.usernameAlreadyExists(signUpDto.getUsername())) {
            throw new EntityExistsException("This username is taken");
        }
        // save user
        ClinicUserDto userDto = ClinicUserDto.builder()
                .username(signUpDto.getUsername())
                .password(signUpDto.getPassword())
                .roleIds(Set.of(signUpDto.getIsDoctor() ? DOCTOR_TYPE_ID : PATIENT_TYPE_ID)).build();

        ClinicUser user = userService.save(userMapper.toEntity(userDto));
        if (signUpDto.getIsDoctor()) { // doctor
            DoctorDto doctorDto = buildDoctorDto(signUpDto, user);
            doctorService.save(doctorMapper.toEntity(doctorDto));
        } else { // patient
            PatientDto patientDto = buildPatientDto(signUpDto, user);
            patientService.save(patientMapper.toEntity(patientDto));
        }
        SignInDto signInDto = new SignInDto();
        signInDto.setPassword(signUpDto.getPassword());
        signInDto.setUsername(signUpDto.getUsername());
        return authenticationService.authenticate(signInDto);
    }

    private DoctorDto buildDoctorDto(SignUpDto signUpDto, ClinicUser user) {
        return DoctorDto.builder()
                .userId(user.getId())
                .email(signUpDto.getEmail())
                .phone(signUpDto.getPhone())
                .name(signUpDto.getFirstName() + " " + signUpDto.getLastName())
                .specialization(signUpDto.getSpecialization())
                .yearsOfExperience(signUpDto.getYearsOfExperience())
                .build();
    }

    private PatientDto buildPatientDto(SignUpDto signUpDto, ClinicUser user) {
        return PatientDto.builder()
                .userId(user.getId())
                .email(signUpDto.getEmail())
                .phone(signUpDto.getPhone())
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .address(signUpDto.getAddress())
                .gender(signUpDto.getGender())
                .dateOfBirth(signUpDto.getDateOfBirth())
                .emergencyContactName(signUpDto.getEmergencyContactName())
                .emergencyContactPhone(signUpDto.getEmergencyContactPhone())
                .build();
    }
}
