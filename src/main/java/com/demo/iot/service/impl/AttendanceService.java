package com.demo.iot.service.impl;

import com.demo.iot.dto.response.AttendanceResponse;
import com.demo.iot.dto.response.UserAttendanceSummaryProjection;
import com.demo.iot.dto.response.UserAttendanceSummaryResponse;
import com.demo.iot.entity.Attendance;
import com.demo.iot.entity.Device;
import com.demo.iot.entity.User;
import com.demo.iot.exception.NotFoundException;
import com.demo.iot.repository.IAttendanceRepository;
import com.demo.iot.repository.IDeviceRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceService implements IAttendanceService {

    IAttendanceRepository attendanceRepository;
    IUserRepository userRepository;
    IDeviceRepository deviceRepository;

    @Override
    public void attendance(String rfidCode, String codeDevice) {
        Optional<User> userOptional = userRepository.findByRfidCode(rfidCode);
        Optional<Device> deviceOptional = deviceRepository.findByCodeDevice(codeDevice);

        if (userOptional.isEmpty() || deviceOptional.isEmpty()) {
            throw new RuntimeException("User or device not found");
        }

        User user = userOptional.get();
        String location = deviceOptional.get().getLocation();
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        Optional<Attendance> existingAttendance = attendanceRepository.findByUserAndDateAndLocation(user, today, location);
        Attendance attendance;

        if (existingAttendance.isPresent()) {
            attendance = existingAttendance.get();
            if (currentTime.isBefore(attendance.getFirstCheckIn())) {
                attendance.setFirstCheckIn(currentTime);
            }
            if (attendance.getLastCheckOut() == null || currentTime.isAfter(attendance.getLastCheckOut())) {
                attendance.setLastCheckOut(currentTime);
            }
        } else {
            attendance = Attendance.builder()
                    .user(user)
                    .date(today)
                    .firstCheckIn(currentTime)
                    .lastCheckOut(null)
                    .location(location)
                    .build();
        }

        attendanceRepository.save(attendance);
    }

    @Override
    public Page<AttendanceResponse> filterAttendance(
            LocalDate startDate,
            LocalDate endDate,
            String studentCode,
            String nameDevice,
            Integer onTime,
            Pageable pageable) {

        // Mốc giờ đúng quy định
        LocalTime checkInTime = LocalTime.of(8, 30);
        LocalTime checkOutTime = LocalTime.of(17, 30);

        // Gọi xuống repository (đã sửa để nhận thêm 2 tham số LocalTime)
        Page<Attendance> attendances = attendanceRepository.filterAttendance(
                startDate, endDate, studentCode, nameDevice, onTime, checkInTime, checkOutTime, pageable);

        List<AttendanceResponse> attendanceResponses = attendances.getContent().stream()
                .map(attendance -> {
                    LocalTime timeIn = attendance.getFirstCheckIn();
                    LocalTime timeOut = attendance.getLastCheckOut();
                    boolean isOnTime = !timeIn.isAfter(checkInTime) && !timeOut.isBefore(checkOutTime);

                    return AttendanceResponse.builder()
                            .rfidCode(attendance.getUser().getRfidCode())
                            .fullName(attendance.getUser().getUsername())
                            .studentCode(attendance.getUser().getStudentCode())
                            .attendanceTimeIn(timeIn)
                            .attendanceTimeOut(timeOut)
                            .date(attendance.getDate().toString())
                            .nameDevice(attendance.getLocation())
                            .onTime(isOnTime)
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(attendanceResponses, pageable, attendances.getTotalElements());
    }

    @Override
    public List<AttendanceResponse> checkUser(String studentCode) {
        User user = userRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new NotFoundException("User not found"));

        LocalDate today = LocalDate.now();
        List<Attendance> attendances = attendanceRepository.findByUserAndDate(user, today);

        if (attendances.isEmpty()) {
            throw new NotFoundException("Not yet checked in today");
        }

        List<AttendanceResponse> responses = new ArrayList<>();
        for (Attendance attendance : attendances) {
            responses.add(AttendanceResponse.builder()
                    .fullName(attendance.getUser().getUsername())
                    .studentCode(attendance.getUser().getStudentCode())
                    .attendanceTimeIn(attendance.getFirstCheckIn())
                    .attendanceTimeOut(attendance.getLastCheckOut())
                    .date(attendance.getDate().toString())
                    .nameDevice(attendance.getLocation())
                    .build());
        }

        return responses;
    }

    @Override
    public Page<AttendanceResponse> statisticByUser(String studentCode, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        User user = userRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Page<Attendance> attendancePage = attendanceRepository.findByUserAndDateRange(user, startDate, endDate, pageable);

        List<AttendanceResponse> responses = attendancePage.getContent().stream()
                .map(attendance -> {
                    LocalTime timeIn = attendance.getFirstCheckIn();
                    LocalTime timeOut = attendance.getLastCheckOut();
                    boolean onTime = timeIn != null && timeOut != null &&
                            !timeIn.isAfter(LocalTime.of(8, 30)) &&     // check-in <= 08:30
                            !timeOut.isBefore(LocalTime.of(17, 30));    // check-out >= 17:30

                    return AttendanceResponse.builder()
                            .rfidCode(user.getRfidCode())
                            .fullName(user.getUsername())
                            .studentCode(user.getStudentCode())
                            .attendanceTimeIn(timeIn)
                            .attendanceTimeOut(attendance.getLastCheckOut())
                            .date(attendance.getDate().toString())
                            .nameDevice(attendance.getLocation())
                            .onTime(onTime)
                            .build();
                })
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, attendancePage.getTotalElements());
    }

    @Override
    public Page<UserAttendanceSummaryResponse> summarizeUserAttendance(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalTime standardIn = LocalTime.of(8, 30);
        LocalTime standardOut = LocalTime.of(17, 30);

        Page<UserAttendanceSummaryProjection> projections = attendanceRepository.summarizeAllUsers(
                startDate, endDate, standardIn, standardOut, pageable);

        List<UserAttendanceSummaryResponse> responses = projections.getContent().stream()
                .map(p -> UserAttendanceSummaryResponse.builder()
                        .fullName(p.getFullName())
                        .studentCode(p.getStudentCode())
                        .onTimeDays(p.getOnTimeDays())
                        .notOnTimeDays(p.getNotOnTimeDays())
                        .build())
                .toList();

        return new PageImpl<>(responses, pageable, projections.getTotalElements());
    }
}