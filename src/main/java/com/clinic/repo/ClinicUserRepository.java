package com.clinic.repo;

import com.clinic.model.ClinicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicUserRepository extends JpaRepository<ClinicUser, Long> {
    Optional<ClinicUser> findByUsername(String username);
}