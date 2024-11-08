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
    public Page<DeviceResponse> getAllDevices(Pageable pageable) {
        Page<Device> devicePage = deviceRepository.findAll(pageable);
        return devicePage.map(deviceMapper::toDeviceResponse);
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
}
