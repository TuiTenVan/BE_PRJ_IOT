package com.demo.iot.entity;

import com.demo.iot.common.Shift;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class Account extends BaseEntity implements UserDetails {
    @Column(nullable = false, length = 50)
    String username;

    @Column(nullable = false, length = 255)
    String password;

    @Column(length = 50)
    String fullName;

    @Column(length = 50)
    String email;

    @Column(length = 50)
    String phone;

    @Column(length = 50)
    String gender;

    @Column(length = 255)
    String address;

    @Column(length = 255)
    String avatar;

    Integer status;

    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    Set<RefreshToken> tokens;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_" + getRole().getName().toUpperCase()));
        return authorityList;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
