package com.demo.iot.repository;

import com.demo.iot.common.Shift;
import com.demo.iot.dto.response.UserAttendanceSummaryProjection;
import com.demo.iot.entity.Attendance;
import com.demo.iot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Optional<Attendance> findByUserAndDateAndLocation(User user, LocalDate date, String location);

    List<Attendance> findByUserAndDate(User user, LocalDate date);

    @Query("SELECT a FROM Attendance a JOIN a.user u " +
            "WHERE (:startDate IS NULL OR a.date >= :startDate) " +
            "AND (:endDate IS NULL OR a.date <= :endDate) " +
            "AND (:employeeCode IS NULL OR u.employeeCode LIKE CONCAT('%', :employeeCode, '%')) " +
            "AND (:location IS NULL OR a.location LIKE CONCAT('%', :location, '%'))")
    Page<Attendance> filterAttendance(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("employeeCode") String employeeCode,
            @Param("location") String location,
            Pageable pageable);

    @Query("SELECT a FROM Attendance a WHERE a.user = :user " +
            "AND (:startDate IS NULL OR a.date >= :startDate) " +
            "AND (:endDate IS NULL OR a.date <= :endDate)")
    Page<Attendance> findByUserAndDateRange(@Param("user") User user,
                                            @Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            Pageable pageable);

    @Query("""
        SELECT
            u.fullName AS fullName,
            u.employeeCode AS employeeCode,
            SUM(CASE
                WHEN a.firstCheckIn <= :standardIn AND a.lastCheckOut >= :standardOut THEN 1
                ELSE 0 END) AS onTimeDays,
            SUM(CASE
                WHEN a.firstCheckIn > :standardIn OR a.lastCheckOut < :standardOut THEN 1
                ELSE 0 END) AS notOnTimeDays
        FROM Attendance a
        JOIN a.user u
        WHERE (:startDate IS NULL OR a.date >= :startDate)
          AND (:endDate IS NULL OR a.date <= :endDate)
          AND (:employeeCode IS NULL OR u.employeeCode LIKE %:employeeCode%)
        GROUP BY u.fullName, u.employeeCode
    """)
    Page<UserAttendanceSummaryProjection> summarizeAllUsers(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("employeeCode") String employeeCode,
            @Param("standardIn") LocalTime standardIn,
            @Param("standardOut") LocalTime standardOut,
            Pageable pageable);
}