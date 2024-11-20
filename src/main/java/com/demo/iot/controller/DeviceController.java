package com.demo.iot.controller;

import com.demo.iot.dto.request.DeviceRequest;
import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.dto.response.DeviceResponse;
import com.demo.iot.service.IDeviceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/device")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeviceController {

    IDeviceService deviceService;

    @GetMapping("/heartbeat")
    public ResponseEntity<?> checkStatus(@RequestParam String codeDevice) {
        deviceService.heartbeat(codeDevice);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_DEVICE')")
    public ResponseEntity<?> createDevice(@RequestBody DeviceRequest deviceRequest) {
        DeviceResponse createdDevice = deviceService.createDevice(deviceRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(createdDevice)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_DEVICES')")
    public ResponseEntity<?> getAllDevices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<DeviceResponse> devicePage = deviceService.getAllDevices(pageable);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(devicePage)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_DEVICE')")
    public ResponseEntity<?> getDeviceById(@PathVariable Integer id) {
        DeviceResponse deviceResponse = deviceService.getDeviceById(id);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(deviceResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_DEVICE')")
    public ResponseEntity<?> updateDevice(@PathVariable Integer id, @RequestBody DeviceRequest deviceRequest) {
        DeviceResponse updatedDevice = deviceService.updateDevice(id, deviceRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(updatedDevice)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_DEVICE')")
    public ResponseEntity<?> deleteDevice(@PathVariable Integer id) {
        deviceService.deleteDevice(id);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
