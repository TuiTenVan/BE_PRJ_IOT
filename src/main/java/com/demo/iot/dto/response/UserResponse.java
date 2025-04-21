package com.demo.iot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer id;
    String rfidCode;
    String username;
    String fullName;
    String gender;
    String employeeCode;
    String phone;
    String email;
    String className;
    Date createdDate;
}
