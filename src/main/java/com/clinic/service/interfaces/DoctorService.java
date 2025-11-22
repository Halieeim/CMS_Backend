package com.clinic.service.interfaces;

import com.clinic.model.Doctor;

import java.util.List;

public interface DoctorService extends BaseService<Doctor> {
    List<Doctor> getDoctorsBySpecialization(String specialization);
    List<Doctor> getDoctorsByMinimumExperience(Integer minYears);
}
