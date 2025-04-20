package com.demo.iot.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAttendanceSummaryResponse {
    String fullName;
    String studentCode;
    int onTimeDays;
    int notOnTimeDays;
}