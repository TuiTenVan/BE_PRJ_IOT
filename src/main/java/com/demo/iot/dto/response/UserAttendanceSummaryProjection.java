package com.demo.iot.dto.response;

public interface UserAttendanceSummaryProjection {
    String getFullName();
    String getEmployeeCode();
    Integer getOnTimeDays();
    Integer getNotOnTimeDays();
}
