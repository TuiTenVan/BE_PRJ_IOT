package com.demo.iot.entity;

import com.demo.iot.common.Shift;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    LocalDate date;

    // Thời gian vào sớm nhất trong ngày
    @Column
    LocalTime firstCheckIn;

    // Thời gian ra muộn nhất trong ngày
    @Column
    LocalTime lastCheckOut;

    @Column
    String location;

    @Column
    Integer onTime;

    @Enumerated(EnumType.STRING)
    private Shift shift;
}