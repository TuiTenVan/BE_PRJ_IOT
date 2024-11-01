package com.demo.iot.controller;

import com.demo.iot.common.Shift;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/shifts")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShiftController {

    @GetMapping("")
    public List<Shift> getShifts() {
        return Arrays.asList(Shift.values());
    }
}
