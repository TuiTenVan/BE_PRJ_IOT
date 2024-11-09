package com.demo.iot.service;

import com.demo.iot.dto.response.AttendanceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IAttendanceService {
    void attendance(String rfidCode, String codeDevice);
    Page<AttendanceResponse> filterAttendance(LocalDate startDate, LocalDate endDate, String shift, String studentCode, String location, Pageable pageable);
    List<AttendanceResponse> checkUser(String studentCode);
}
