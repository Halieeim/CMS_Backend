package com.clinic.controller.mapper;

import com.clinic.model.Authority;
import com.clinic.repo.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapperUtils {
    private final AuthorityRepository authorityRepository;

    @Named("mapIdsToAuthorities")
    public Set<Authority> mapIdsToAuthorities(Set<Long> authorityIds) {
        if (authorityIds == null) {
            return Set.of();
        }
        return authorityIds.stream()
                .map(authorityRepository::findById)
                .filter(opt -> opt.isPresent())
                .map(opt -> opt.get())
                .collect(Collectors.toSet());
    }

    @Named("mapAuthoritiesToIds")
    public Set<Long> mapAuthoritiesToIds(Set<Authority> authorities) {
        if (authorities == null) {
            return Set.of();
        }
        return authorities.stream()
                .map(Authority::getId)
                .collect(Collectors.toSet());
    }
}