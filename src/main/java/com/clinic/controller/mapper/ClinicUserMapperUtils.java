package com.clinic.controller.mapper;

import com.clinic.model.Role;
import com.clinic.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClinicUserMapperUtils {
    private final RoleRepository roleRepository;

    @Named("mapRoleIdsToRoles")
    public Set<Role> mapRoleIdsToRoles(Set<Long> roleIds) {
        if (roleIds == null) {
            return Set.of();
        }
        return roleIds.stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Named("mapRolesToRoleIds")
    public Set<Long> mapRolesToRoleIds(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }
}