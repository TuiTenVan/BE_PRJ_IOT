package com.demo.iot.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Device extends BaseEntity {
    @Column
    String name;

    @Column
    String location;

    @Column
    String codeDevice;

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY)
    List<DeviceUser> deviceUsers;
}
