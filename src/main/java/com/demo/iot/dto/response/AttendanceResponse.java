package com.demo.iot.dto.response;

import com.demo.iot.common.Shift;
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
    String studentCode;
    String date;
    String nameDevice;
    LocalTime attendanceTimeOut;
    Boolean onTime;
}
