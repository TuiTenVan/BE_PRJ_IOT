package com.demo.iot.controller;

import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.service.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/rfid")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RfidController {
    IUserService userService;
    SimpMessagingTemplate messagingTemplate;

    @PostMapping("")
    public ResponseEntity<?> createRfid(@RequestParam("rfidCode") String rfidCode,
                                        @RequestParam("deviceCode") String deviceCode) {
        userService.createRfid(rfidCode, deviceCode);
        try {
            messagingTemplate.convertAndSend("/topic/rfid-created", rfidCode);
        } catch (Exception e) {
            log.error("Lỗi khi gửi WebSocket RFID created: ", e);
        }
        log.info("RFID created successfully: {}", rfidCode);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{rfid-code}")
    public ResponseEntity<?> deleteRfid(@PathVariable("rfid-code") String rfidCode,
                                        @RequestParam(value = "deviceCode") String deviceCode){
        userService.deleteRfid(rfidCode, deviceCode);
        try {
            messagingTemplate.convertAndSend("/topic/rfid-deleted", rfidCode);
        } catch (Exception e) {
            log.error("Lỗi khi gửi WebSocket RFID deleted: ", e);
        }
        log.info("RFID deleted successfully: {}", rfidCode);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
