package com.demo.iot.service;

import com.demo.iot.dto.request.GpsDataRequest;
import com.demo.iot.dto.response.DeviceResponse;

public interface IGpsService {
    DeviceResponse saveGpsData(GpsDataRequest gpsData);
}
