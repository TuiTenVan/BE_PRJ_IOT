package com.defty.iot.controller;

import com.defty.iot.dto.request.RoleRequest;
import com.defty.iot.dto.response.ApiResponse;
import com.defty.iot.dto.response.RoleResponse;
import com.defty.iot.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("${api.prefix}/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    IRoleService roleService;

    @GetMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ALL_ROLE')")
    public ResponseEntity<?> getRoles() {
        Set<RoleResponse> roleResponses = roleService.getAllRoles();
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Roles retrieved successfully")
                .data(roleResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("@requiredPermission.checkPermission('GET_ROLE')")
    public ResponseEntity<?> getRoleId(@PathVariable("roleId") Integer roleId) {
        RoleResponse roleResponse = roleService.getRoleId(roleId);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("@requiredPermission.checkPermission('CREATE_ROLE')")
    public ResponseEntity<?> createRole(@RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.createRole(roleRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(HttpStatus.CREATED.getReasonPhrase())
                .data(roleResponse.getId())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@requiredPermission.checkPermission('UPDATE_ROLE')")
    public ResponseEntity<?> updateRole(@PathVariable Integer id,
                                        @RequestBody RoleRequest roleRequest) {
        RoleResponse roleResponse = roleService.updateRole(id, roleRequest);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Role updated successfully")
                .data(roleResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/assignment/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('ASSIGN_PERMISSION_TO_ROLE')")
    public ResponseEntity<?> assignPermissions(@RequestParam Integer roleId,
                                               @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = roleService.assignPermissionToRole(roleId, permissionIds);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/unassignment/{permissionIds}")
    @PreAuthorize("@requiredPermission.checkPermission('UNASSIGN_PERMISSION_FROM_ROLE')")
    public ResponseEntity<?> unassignPermissions(@RequestParam Integer roleId,
                                                 @PathVariable List<Integer> permissionIds) {
        RoleResponse roleResponse = roleService.unassignPermissionFromRole(roleId, permissionIds);
        ApiResponse<?> response = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .data(roleResponse)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
