package com.demo.iot.controller;

import com.demo.iot.dto.request.PermissionRequest;
import com.demo.iot.dto.response.ApiResponse;
import com.demo.iot.dto.response.PermissionResponse;
import com.demo.iot.service.IPermissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/permission")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    IPermissionService permissionService;

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_PERMISSIONS')")
    public ResponseEntity<?> getAllPermissions(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                               @RequestParam(value = "name", required = false) String name) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(page, size);
        Page<PermissionResponse> permissionResponses = permissionService.getAllPermissions(name, pageable);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(permissionResponses)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{permissionId}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_PERMISSION')")
    public ResponseEntity<?> getPermissionById(@PathVariable("permissionId") Integer permissionId) {
        PermissionResponse permissionResponse = permissionService.getPermissionById(permissionId);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(permissionResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_PERMISSION')")
    public ResponseEntity<?> createPermission(@RequestBody PermissionRequest permissionRequest) {
        PermissionResponse permissionResponse = permissionService.createPermission(permissionRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(permissionResponse.getId())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{permissionId}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_PERMISSION')")
    public ResponseEntity<?> updatePermission(@PathVariable("permissionId") Integer permissionId,
                                              @RequestBody PermissionRequest permissionRequest) {
        permissionService.updatePermission(permissionId, permissionRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data("success")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('DELETE_PERMISSION')")
    public ResponseEntity<?> unassignPermission(@PathVariable List<Integer> permissionIds) {
        permissionService.deletePermissions(permissionIds);
        return ResponseEntity.ok().build();
    }
}
