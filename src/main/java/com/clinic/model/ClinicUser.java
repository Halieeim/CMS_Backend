package com.clinic.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
            ,uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","role_id"})
    )
    private Set<Role> roles = new HashSet<>();

    public void addRoles(Role... roles) {
        this.roles.addAll(Set.of(roles));
    }
    public void addRoles(Set<Role> roles) {
        this.roles.addAll(roles);
    }

}