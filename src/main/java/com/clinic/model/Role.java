package com.clinic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_authorities"
            ,joinColumns = @JoinColumn(name = "role_id",nullable = false)
            ,inverseJoinColumns = @JoinColumn(name = "authority_id",nullable = false)
            ,uniqueConstraints = @UniqueConstraint(columnNames = {"role_id","authority_id"}))
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority...authority) {
        this.authorities.addAll(Set.of(authority));
    }
    public void addAuthority(Set<Authority> authorities) {
        this.authorities.addAll(authorities);
    }
    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", getName());
    }
}
