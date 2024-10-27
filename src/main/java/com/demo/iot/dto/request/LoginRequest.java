package com.demo.iot.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LoginRequest {
    String username;
    String password;
}
