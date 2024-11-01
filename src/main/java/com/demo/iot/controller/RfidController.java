package com.demo.iot.controller;

import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/rfid")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RfidController {
    IUserService userService;

    @PostMapping("")
    public ResponseEntity<?> createRfid(@RequestParam String rfidCode){
        userService.createRfid(rfidCode);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{rfid-code}")
    public ResponseEntity<?> deleteRfid(@PathVariable("rfid-code") String rfidCode){
        userService.deleteRfid(rfidCode);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
