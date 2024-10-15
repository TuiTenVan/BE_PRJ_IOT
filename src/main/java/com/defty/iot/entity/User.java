package com.defty.iot.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class User extends BaseEntity {
    @Column(length = 100)
    private String username;

    @Column(name = "Student_Code", length = 50)
    private String studentCode;

    @Column(name = "Class", length = 50)
    private String className;

    @Column(name = "RFID_Code", length = 50)
    private String rfidCode;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Gender", length = 100)
    private String gender;

    @Column(name = "Phone", length = 20)
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

}
