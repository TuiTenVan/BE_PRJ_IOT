package com.defty.iot.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Attendance extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "User_ID", nullable = false)
    private User user;

    @Column(name = "Timestamp")
    private LocalDateTime timestamp;

    @Column(name = "Status", length = 10)
    private String status;

}
