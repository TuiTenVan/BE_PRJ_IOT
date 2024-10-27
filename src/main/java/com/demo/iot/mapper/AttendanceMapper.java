package com.demo.iot.mapper;

import com.demo.iot.dto.response.AttendanceResponse;
import com.demo.iot.entity.Attendance;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceMapper {
    ModelMapper modelMapper;

//    public AttendanceResponse toAttendanceResponse(Attendance attendance) {
//        AttendanceResponse attendanceResponse = new AttendanceResponse();
//        attendanceResponse.
//    }
}
