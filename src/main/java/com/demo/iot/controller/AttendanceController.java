package com.demo.iot.controller;

import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.dto.response.AttendanceResponse;
import com.demo.iot.dto.response.UserAttendanceSummaryResponse;
import com.demo.iot.service.IAttendanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/attendance")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceController {
    IAttendanceService attendanceService;
    SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestParam String rfidCode, @RequestParam String codeDevice) {
        attendanceService.attendance(rfidCode, codeDevice);
        try {
            simpMessagingTemplate.convertAndSend("/topic/attendance", rfidCode);
            log.info("Socket attendance: {}", rfidCode);
        } catch (Exception e) {
            log.error("Lỗi khi gửi WebSocket attendance: ", e);
        }
        log.info("Attendance successfully: {}", rfidCode);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Attendance recorded successfully")
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/filter")
    @PreAuthorize("@requiredPermission.checkPermission('HISTORY_CHECKIN')")
    public ResponseEntity<?> filterAttendance(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "employeeCode", required = false) String employeeCode,
            @RequestParam(value = "nameDevice", required = false) String nameDevice) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<AttendanceResponse> filteredAttendance = attendanceService.filterAttendance(startDate, endDate, null, employeeCode, nameDevice, pageable);

        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Filtered attendance fetched")
                .data(filteredAttendance)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistic/{employeeCode}")
    @PreAuthorize("@requiredPermission.checkPermission('HISTORY_CHECKIN')")
    public ResponseEntity<?> statisticAttendance(
            @PathVariable String employeeCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<AttendanceResponse> result = attendanceService.statisticByUser(employeeCode, startDate, endDate, pageable);

        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Statistic by user fetched successfully")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    @PreAuthorize("@requiredPermission.checkPermission('HISTORY_CHECKIN')")
    public ResponseEntity<?> summaryAttendanceAllUsers(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserAttendanceSummaryResponse> result = attendanceService.summarizeUserAttendance(startDate, endDate, pageable);

        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Summary attendance for all users fetched successfully")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}