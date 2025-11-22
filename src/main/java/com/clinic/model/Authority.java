package com.clinic.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private static final long serialVersionUID = 3167450423840556878L;

    private String name;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
