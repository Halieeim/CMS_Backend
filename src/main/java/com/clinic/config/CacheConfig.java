package com.clinic.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("doctors"),
                new ConcurrentMapCache("allDoctors"),
                new ConcurrentMapCache("doctorsBySpecialization"),
                new ConcurrentMapCache("doctorsByExperience"),
                new ConcurrentMapCache("patients"),
                new ConcurrentMapCache("allPatients"),
                new ConcurrentMapCache("appointments"),
                new ConcurrentMapCache("allAppointments"),
                new ConcurrentMapCache("medicalRecords"),
                new ConcurrentMapCache("allMedicalRecords"),
                new ConcurrentMapCache("bills"),
                new ConcurrentMapCache("allBills"),
                new ConcurrentMapCache("prescriptionItems"),
                new ConcurrentMapCache("allPrescriptionItems"),
                new ConcurrentMapCache("users"),
                new ConcurrentMapCache("allUsers"),
                new ConcurrentMapCache("roles"),
                new ConcurrentMapCache("allRoles"),
                new ConcurrentMapCache("authorities"),
                new ConcurrentMapCache("allAuthorities")
        ));
        return cacheManager;
    }
}