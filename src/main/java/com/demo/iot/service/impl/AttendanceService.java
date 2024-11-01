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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceService implements IAttendanceService {
    IAttendanceRepository attendanceRepository;
    IUserRepository userRepository;

    public void attendance(String rfidCode) {
        Optional<User> userOptional = userRepository.findByRfidCode(rfidCode);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            LocalDate today = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            Shift shift = null;
            if (currentTime.isAfter(LocalTime.of(8, 0)) && currentTime.isBefore(LocalTime.of(11, 0))) {
                shift = Shift.Morning;
            } else if (currentTime.isAfter(LocalTime.of(14, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
                shift = Shift.Afternoon;
            }
            else{
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
    public List<AttendanceResponse> filterAttendance(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        List<Attendance> attendanceList = attendanceRepository.findAttendanceByDateAndTime(startDate, endDate, startTime, endTime);
        List<AttendanceResponse> attendanceResponseList = new ArrayList<>();
        for (Attendance attendance : attendanceList) {
            AttendanceResponse attendanceResponse = AttendanceResponse.builder()
                    .rfidCode(attendance.getUser().getRfidCode())
                    .fullName(attendance.getUser().getUsername())
                    .attendanceTimeIn(attendance.getTimeIn())
                    .attendanceTimeOut(attendance.getTimeOut())
                    .build();
            attendanceResponseList.add(attendanceResponse);
        }
        return attendanceResponseList;
    }
}
