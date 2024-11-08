package com.demo.iot.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequest {
    String username;
    String email;
    String fullName;
    String phone;
    String gender;
    String address;
    String avatar;
    LocalDate dateOfBirth;
    String password;
    String role;
}
