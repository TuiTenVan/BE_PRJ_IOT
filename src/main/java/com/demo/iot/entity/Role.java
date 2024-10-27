package com.demo.iot.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class Role extends BaseEntity {
    @Column(nullable = false, length = 255)
    String name;

    @Column(length = 255)
    String description;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    Set<Account> accounts;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    Set<RolePermission> rolePermissions;

}
