package com.demo.iot.controller;

import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.dto.response.AttendanceResponse;
import com.demo.iot.service.IAttendanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/check")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCheckController {
    IAttendanceService attendanceService;

    @GetMapping("")
    public ResponseEntity<?> check(@RequestParam(value = "employeeCode") String employeeCode){
        List<AttendanceResponse> attendanceResponseList = attendanceService.checkUser(employeeCode);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(attendanceResponseList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
