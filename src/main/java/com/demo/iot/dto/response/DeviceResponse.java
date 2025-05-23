package com.demo.iot.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceResponse {
    Integer id;
    String name;
    String location;
    String codeDevice;
    String status;
    Date createdDate;
    String longitude;
    String latitude;
    String googleMapLink;
}
