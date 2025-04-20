package com.demo.iot.dto.response;

public interface UserAttendanceSummaryProjection {
    String getFullName();
    String getStudentCode();
    Integer getOnTimeDays();
    Integer getNotOnTimeDays();
}
