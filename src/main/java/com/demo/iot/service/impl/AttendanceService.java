package com.demo.iot.service.impl;

import com.demo.iot.common.Shift;
import com.demo.iot.dto.response.AttendanceResponse;
import com.demo.iot.entity.Attendance;
import com.demo.iot.entity.User;
import com.demo.iot.repository.IAttendanceRepository;
import com.demo.iot.repository.IUserRepository;
import com.demo.iot.service.IAttendanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceService implements IAttendanceService {
    IAttendanceRepository attendanceRepository;
    IUserRepository userRepository;

    public void attendance(String rfidCode) {
        Optional<User> userOptional = userRepository.findByRfidCode(rfidCode);
        if (userOptional.isPresent() && userOptional.get().getUsername() != null && userOptional.get().getStudentCode() != null) {
            User user = userOptional.get();
            LocalDate today = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            Shift shift;
            if (currentTime.isAfter(LocalTime.of(8, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
                shift = Shift.Morning;
            } else if (currentTime.isAfter(LocalTime.of(14, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
                shift = Shift.Afternoon;
            } else{
                shift = Shift.OverTime;
            }
            Optional<Attendance> existingAttendance = attendanceRepository.findByUserAndDateAndShift(user, today, shift);
            Attendance attendance;
            if (existingAttendance.isPresent()) {
                attendance = existingAttendance.get();
                attendance.setTimeOut(currentTime);
            } else {
                attendance = getAttendance(user, today, currentTime, shift);
            }
            attendanceRepository.save(attendance);
        }
        else{
            throw new RuntimeException("RFID code has no information");
        }
    }

    private static Attendance getAttendance(User user, LocalDate today, LocalTime currentTime, Shift shift) {
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(today);
        attendance.setTimeIn(currentTime);
        attendance.setShift(shift);
        boolean onTime = false;
        if (shift == Shift.Morning) {
            onTime = !currentTime.isAfter(LocalTime.of(8, 0));
        } else if (shift == Shift.Afternoon) {
            onTime = !currentTime.isAfter(LocalTime.of(17, 0));
        }
        attendance.setOnTime(onTime);
        return attendance;
    }


    @Override
    public Page<AttendanceResponse> filterAttendance(LocalDate startDate, LocalDate endDate, String shift, String username, String location, Pageable pageable) {
        Page<Attendance> attendances;
        if (startDate == null && endDate == null && shift == null && username == null) {
            attendances = attendanceRepository.findAll(pageable);
        } else {
            if(shift != null){
                Shift convertShift = Shift.valueOf(shift);
                attendances = attendanceRepository.filterAttendance(startDate, endDate, convertShift, username, location, pageable);
            }
            else{
                attendances = attendanceRepository.filterAttendance(startDate, endDate, null, username, location,  pageable);
            }
        }
        List<AttendanceResponse> attendanceResponseList = attendances.getContent().stream()
                .map(attendance -> AttendanceResponse.builder()
                        .rfidCode(attendance.getUser().getRfidCode())
                        .fullName(attendance.getUser().getUsername())
                        .attendanceTimeIn(LocalTime.parse(attendance.getTimeIn().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                        .date(attendance.getDate().toString())
                        .shift(attendance.getShift())
                        .attendanceTimeOut(attendance.getTimeOut() != null ?
                                LocalTime.parse(attendance.getTimeOut().format(DateTimeFormatter.ofPattern("HH:mm:ss"))) : null)
                        .onTime(String.valueOf(attendance.isOnTime()))
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(attendanceResponseList, pageable, attendances.getTotalElements());
    }

}
