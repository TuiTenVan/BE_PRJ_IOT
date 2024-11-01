package com.demo.iot.repository;

import com.demo.iot.common.Shift;
import com.demo.iot.entity.Attendance;
import com.demo.iot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByUserAndDateAndShift(User user, LocalDate date, Shift shift);

    @Query("SELECT a FROM Attendance a WHERE a.date BETWEEN :startDate AND :endDate " +
            "AND a.timeIn BETWEEN :startTime AND :endTime")
    List<Attendance> findAttendanceByDateAndTime(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
