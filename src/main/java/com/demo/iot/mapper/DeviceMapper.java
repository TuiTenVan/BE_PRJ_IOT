package com.demo.iot.mapper;

import com.demo.iot.dto.request.DeviceRequest;
import com.demo.iot.dto.response.DeviceResponse;
import com.demo.iot.entity.Device;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceMapper {
    ModelMapper modelMapper;
    public Device toDevice(DeviceRequest deviceRequest) {
        return modelMapper.map(deviceRequest, Device.class);
    }

    public DeviceResponse toDeviceResponse(Device device) {
        return modelMapper.map(device, DeviceResponse.class);
    }
}
