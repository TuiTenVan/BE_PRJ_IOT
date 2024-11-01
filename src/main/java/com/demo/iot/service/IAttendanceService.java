package com.demo.iot.service;

import com.demo.iot.dto.response.AttendanceResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IAttendanceService {
    void attendance(String rfidCode);
    List<AttendanceResponse> filterAttendance(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime);
}
