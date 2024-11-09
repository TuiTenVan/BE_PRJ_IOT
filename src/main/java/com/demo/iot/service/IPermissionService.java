package com.demo.iot.service;

import com.demo.iot.dto.request.PermissionRequest;
import com.demo.iot.dto.response.PermissionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    Page<PermissionResponse> getAllPermissions(String search, Pageable pageable);
    void deletePermissions(List<Integer> permissionIds);
    void updatePermission(Integer permissionId, PermissionRequest permissionRequest);
    PermissionResponse getPermissionById(Integer permissionId);
}
