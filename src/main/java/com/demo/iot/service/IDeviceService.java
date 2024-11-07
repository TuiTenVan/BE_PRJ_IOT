package com.demo.iot.service;

import com.demo.iot.dto.request.DeviceRequest;
import com.demo.iot.dto.response.DeviceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDeviceService {
    DeviceResponse createDevice(DeviceRequest device);
    Page<DeviceResponse> getAllDevices(Pageable pageable);
    DeviceResponse updateDevice(Integer id, DeviceRequest device);
    void deleteDevice(Integer id);
    DeviceResponse getDeviceById(Integer id);
}
