package com.demo.iot.service.impl;

import com.demo.iot.dto.request.GpsDataRequest;
import com.demo.iot.dto.response.DeviceResponse;
import com.demo.iot.entity.Device;
import com.demo.iot.mapper.DeviceMapper;
import com.demo.iot.repository.IDeviceRepository;
import com.demo.iot.service.IGpsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GpsService implements IGpsService {
    private final IDeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Override
    public DeviceResponse saveGpsData(GpsDataRequest gpsData) {
        Optional<Device> device = deviceRepository.findByCodeDevice(gpsData.getCodeDevice());
        DeviceResponse deviceResponse;
        if(device.isPresent()) {
            Device deviceEntity = device.get();
            String latitude = gpsData.getLatitude();
            String longitude = gpsData.getLongitude();

            // Tạo liên kết Google Maps
            String googleMapLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;
            deviceEntity.setLatitude(latitude);
            deviceEntity.setLongitude(longitude);
            deviceEntity.setGoogleMapLink(googleMapLink);

            deviceRepository.save(deviceEntity);
            gpsData.setGoogleMapLink(googleMapLink);
            deviceResponse = deviceMapper.toDeviceResponse(deviceEntity);
        } else {
            log.warn("Device not found for code: {}", gpsData.getCodeDevice());
            throw new RuntimeException("Device not found");
        }
        return deviceResponse;
    }
}
