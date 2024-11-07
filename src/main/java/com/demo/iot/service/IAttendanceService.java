package com.demo.iot.service;

import com.demo.iot.dto.response.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IAttendanceService {
    void attendance(String rfidCode, String codeDevice);
    Page<AttendanceResponse> filterAttendance(LocalDate startDate, LocalDate endDate, String shift, String username, String location, Pageable pageable);
}
