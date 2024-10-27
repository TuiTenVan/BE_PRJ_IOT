//package com.defty.iot.controller;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("${api.prefix}/device")
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class DeviceController {
//    private DeviceService deviceService;
//
//    @PostMapping
//    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
//        return ResponseEntity.ok(deviceService.createDevice(device));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Device>> getAllDevices() {
//        return ResponseEntity.ok(deviceService.getAllDevices());
//    }
//}