package com.demo.iot.service;

import com.demo.iot.dto.request.DeviceRequest;
import com.demo.iot.dto.response.DeviceResponse;

import java.util.List;

public interface IDeviceService {
    DeviceResponse createDevice(DeviceRequest device);
    List<DeviceResponse> getAllDevices();
    DeviceResponse updateDevice(Integer id, DeviceRequest device);
    void deleteDevice(Integer id);
}
