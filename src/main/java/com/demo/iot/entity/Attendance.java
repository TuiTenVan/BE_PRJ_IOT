package com.demo.iot.entity;

import com.demo.iot.common.Shift;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Attendance{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column
    LocalDate date;

    @Column
    LocalTime timeIn;

    @Column
    LocalTime timeOut;

    @Enumerated(EnumType.STRING)
    Shift shift;

    @Column
    boolean onTime;

    @Column
    boolean leftEarly;

    @Column
    String location;
}
