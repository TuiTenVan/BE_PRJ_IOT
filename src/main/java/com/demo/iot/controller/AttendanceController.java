package com.demo.iot.controller;

import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.dto.response.AttendanceResponse;
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

    @PostMapping
    public ResponseEntity<?> createAttendance(@RequestParam String rfidCode, @RequestParam String codeDevice) {
        attendanceService.attendance(rfidCode, codeDevice);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/filter")
    @PreAuthorize("@requiredPermission.checkPermission('HISTORY_CHECKIN')")
    public ResponseEntity<?> filterAttendance(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "shift", required = false) String shift,
            @RequestParam(value = "username", required = false) String username, @RequestParam(value = "location", required = false) String location) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "date"));
        Page<AttendanceResponse> filteredAttendance = attendanceService.filterAttendance(startDate, endDate, shift, username, location, pageable);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(filteredAttendance)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
