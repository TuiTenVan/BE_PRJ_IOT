package com.demo.iot.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String username;
    String fullName;
    String email;
    String phone;
    String className;
    String gender;
    String employeeCode;
    String fullName;
}
