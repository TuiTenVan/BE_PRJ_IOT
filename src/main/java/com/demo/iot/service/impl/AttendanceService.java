package com.demo.iot.service.impl;

import com.demo.iot.common.Shift;
import com.demo.iot.dto.response.AttendanceResponse;
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
import java.time.format.DateTimeFormatter;
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

    public void attendance(String rfidCode, String codeDevice) {
        Optional<User> userOptional = userRepository.findByRfidCode(rfidCode);
        Optional<Device> deviceOptional = deviceRepository.findByCodeDevice(codeDevice);

        if (userOptional.isPresent() && userOptional.get().getUsername() != null && userOptional.get().getStudentCode() != null) {
            LocalDate today = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            User user = userOptional.get();

            String location = deviceOptional.get().getLocation();
            Shift shift;

            if (currentTime.isAfter(LocalTime.of(6, 0)) && currentTime.isBefore(LocalTime.of(12, 0))) { // 8 den 12
                shift = Shift.Morning;
            } else if (currentTime.isAfter(LocalTime.of(12, 0)) && currentTime.isBefore(LocalTime.of(18, 0))) { // 14 den 18
                shift = Shift.Afternoon;
            } else { // 19 den 6
                shift = Shift.OverTime;
            }

            Optional<Attendance> existingAttendance = attendanceRepository.findByUserAndDateAndShiftAndLocation(user, today, shift, location);
            Attendance attendance;
            if (existingAttendance.isPresent()) {
                attendance = existingAttendance.get();
                attendance.setTimeOut(currentTime);
            } else {
                attendance = getAttendance(user, today, currentTime, shift, deviceOptional.get());
            }
            attendanceRepository.save(attendance);
        }
        else{
            throw new RuntimeException("RFID code has no information");
        }
    }

    private static Attendance getAttendance(User user, LocalDate today, LocalTime currentTime, Shift shift, Device device) {
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setDate(today);
        attendance.setTimeIn(currentTime);
        attendance.setShift(shift);
        attendance.setLocation(device.getLocation());
        boolean onTime = false;
        if (shift == Shift.Morning) {
            onTime = !currentTime.isAfter(LocalTime.of(8, 0));
        } else if (shift == Shift.Afternoon) {
            onTime = !currentTime.isAfter(LocalTime.of(14, 0));
        } else {
            onTime = (currentTime.isBefore(LocalTime.of(19, 0)) && currentTime.isAfter(LocalTime.of(18, 0)));
        }
        attendance.setOnTime(onTime);
        return attendance;
    }

    @Override
    public Page<AttendanceResponse> filterAttendance(LocalDate startDate, LocalDate endDate, String shift, String studentCode, String nameDevice, Pageable pageable) {
        Page<Attendance> attendances;
        if (startDate == null && endDate == null && shift == null && studentCode == null && nameDevice == null) {
            attendances = attendanceRepository.findAll(pageable);
        } else {
            if(shift != null){
                Shift convertShift = Shift.valueOf(shift);
                attendances = attendanceRepository.filterAttendance(startDate, endDate, convertShift, studentCode, nameDevice, pageable);
            }
            else{
                attendances = attendanceRepository.filterAttendance(startDate, endDate, null, studentCode, nameDevice,  pageable);
            }
        }
        List<AttendanceResponse> attendanceResponseList = attendances.getContent().stream()
                .map(attendance -> AttendanceResponse.builder()
                        .rfidCode(attendance.getUser().getRfidCode())
                        .fullName(attendance.getUser().getUsername())
                        .attendanceTimeIn(LocalTime.parse(attendance.getTimeIn().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                        .date(attendance.getDate().toString())
                        .shift(attendance.getShift())
                        .nameDevice(attendance.getLocation())
                        .studentCode(attendance.getUser().getStudentCode())
                        .attendanceTimeOut(attendance.getTimeOut() != null ?
                                LocalTime.parse(attendance.getTimeOut().format(DateTimeFormatter.ofPattern("HH:mm:ss"))) : null)
                        .onTime(attendance.isOnTime())
                        .build())
                .collect(Collectors.toList());
        return new PageImpl<>(attendanceResponseList, pageable, attendances.getTotalElements());
    }

    @Override
    public List<AttendanceResponse> checkUser(String studentCode) {
        Optional<User> userOptional = userRepository.findByStudentCode(studentCode);
        if(userOptional.isEmpty()){
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();
        LocalDate today = LocalDate.now();
        List<Attendance> attendances = attendanceRepository.findByUserAndDate(user, today);
        if (attendances.isEmpty()){
            throw new NotFoundException("Not yet checked");
        }
        List<AttendanceResponse> attendanceResponseList = new ArrayList<>();
        for(Attendance attendance : attendances){
            AttendanceResponse attendanceResponse = AttendanceResponse.builder()
                    .fullName(attendance.getUser().getUsername())
                    .onTime(attendance.isOnTime())
                    .attendanceTimeIn(LocalTime.parse(attendance.getTimeIn().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                    .attendanceTimeOut(attendance.getTimeOut() != null ?
                            LocalTime.parse(attendance.getTimeOut().format(DateTimeFormatter.ofPattern("HH:mm:ss"))) : null)
                    .date(String.valueOf(attendance.getDate()))
                    .shift(attendance.getShift())
                    .nameDevice(attendance.getLocation())
                    .build();
            attendanceResponseList.add(attendanceResponse);
        }
        return attendanceResponseList;
    }

}
