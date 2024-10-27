package com.demo.iot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceResponse {
    String rfidCode;
    String fullName;
    LocalTime attendanceTimeIn;
    String attendanceStatus;
    LocalTime attendanceTimeOut;
}
