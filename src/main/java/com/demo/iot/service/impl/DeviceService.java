package com.demo.iot.service.impl;

import com.demo.iot.dto.request.DeviceRequest;
import com.demo.iot.dto.response.DeviceResponse;
import com.demo.iot.entity.Device;
import com.demo.iot.exception.AlreadyExitException;
import com.demo.iot.exception.NotFoundException;
import com.demo.iot.mapper.DeviceMapper;
import com.demo.iot.repository.IDeviceRepository;
import com.demo.iot.service.IDeviceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceService implements IDeviceService {
    IDeviceRepository deviceRepository;
    DeviceMapper deviceMapper;

    @Override
    @Transactional
    public DeviceResponse createDevice(DeviceRequest deviceRequest) {
        if (deviceRepository.findByName(deviceRequest.getName()).isPresent()) {
            throw new AlreadyExitException("Device with name " + deviceRequest.getName() + " already exists");
        }
        if (deviceRepository.findByLocation(deviceRequest.getLocation()).isPresent()) {
            throw new AlreadyExitException("Device with location " + deviceRequest.getLocation() + " already exists");
        }
        if (deviceRepository.findByCodeDevice(deviceRequest.getCodeDevice()).isPresent()) {
            throw new AlreadyExitException("Device with code " + deviceRequest.getCodeDevice() + " already exists");
        }
        Device device = deviceMapper.toDevice(deviceRequest);
        device = deviceRepository.save(device);
        return deviceMapper.toDeviceResponse(device);
    }

    @Override
    @Transactional
    public Page<DeviceResponse> getAllDevices(Pageable pageable) {
        Page<Device> devicePage = deviceRepository.findAll(pageable);
        List<Device> devicesToUpdate = new ArrayList<>();
        devicePage.forEach(device -> {
            boolean isOnline = checkOnline(device.getModifiedDate());
            String newStatus = isOnline ? "online" : "offline";
            if (!newStatus.equals(device.getStatus())) {
                device.setStatus(newStatus);
                devicesToUpdate.add(device);
            }
        });
        if (!devicesToUpdate.isEmpty()) {
            deviceRepository.saveAll(devicesToUpdate);
        }
        return devicePage.map(deviceMapper::toDeviceResponse);
    }

    private boolean checkOnline(LocalDateTime modifiedDate) {
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.systemDefault());
        ZonedDateTime deviceModifiedTime = modifiedDate.atZone(ZoneId.systemDefault());
        return deviceModifiedTime.isAfter(now.minusSeconds(30));
    }


    @Override
    @Transactional
    public DeviceResponse updateDevice(Integer id, DeviceRequest deviceRequest) {
        Optional<Device> optionalDevice = deviceRepository.findById(id);
        if (optionalDevice.isEmpty()) {
            throw new NotFoundException("Device not found");
        }
        Device device = optionalDevice.get();
        device.setName(deviceRequest.getName());
        device.setLocation(deviceRequest.getLocation());
        device.setCodeDevice(deviceRequest.getCodeDevice());
        device = deviceRepository.save(device);
        return deviceMapper.toDeviceResponse(device);
    }

    @Override
    @Transactional
    public void deleteDevice(Integer id) {
        if (!deviceRepository.existsById(id)) {
            throw new NotFoundException("Device not found");
        }
        deviceRepository.deleteById(id);
    }

    @Override
    public DeviceResponse getDeviceById(Integer id) {
        Optional<Device> optionalDevice = deviceRepository.findById(id);
        if (optionalDevice.isEmpty()) {
            throw new NotFoundException("Device not found");
        }
        return deviceMapper.toDeviceResponse(optionalDevice.get());
    }

    @Override
    public void heartbeat(String codeDevice) {
        Optional<Device> optionalDevice = deviceRepository.findByCodeDevice(codeDevice);
        if (optionalDevice.isEmpty()) {
            throw new NotFoundException("Device not found");
        }
        Device device = optionalDevice.get();
        device.setStatus("online");
        device.setModifiedDate(LocalDateTime.now());
        deviceRepository.save(device);
    }
}
