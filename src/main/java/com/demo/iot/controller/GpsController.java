package com.demo.iot.controller;

import com.demo.iot.dto.request.GpsDataRequest;
import com.demo.iot.dto.response.DeviceResponse;
import com.demo.iot.service.IGpsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/gps")
@RequiredArgsConstructor
@Slf4j
public class GpsController {
    private final IGpsService gpsService;

    @PostMapping("/location")
    public ResponseEntity<String> receiveGpsData(@RequestBody GpsDataRequest gpsData) {
        DeviceResponse deviceResponse = gpsService.saveGpsData(gpsData);
        log.info("GPS data received: {}", deviceResponse.getGoogleMapLink());
        return ResponseEntity.ok("GPS data received: {}" + deviceResponse.getGoogleMapLink());
    }
}
