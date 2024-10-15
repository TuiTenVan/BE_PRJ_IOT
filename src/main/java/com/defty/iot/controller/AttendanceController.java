//package com.defty.iot.controller;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("${api.prefix}/attendance")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class AttendanceController {
//    private AttendanceService attendanceService;
//
//    @PostMapping
//    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
//        return ResponseEntity.ok(attendanceService.createAttendance(attendance));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Attendance>> getAllAttendance() {
//        return ResponseEntity.ok(attendanceService.getAllAttendance());
//    }
//}
