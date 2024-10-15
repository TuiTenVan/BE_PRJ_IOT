package com.defty.iot.service;

import com.defty.iot.dto.request.PermissionRequest;
import com.defty.iot.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest permissionRequest);
    List<PermissionResponse> getAllPermissions();
    void deletePermissions(List<Integer> permissionIds);
    void updatePermission(Integer permissionId, PermissionRequest permissionRequest);
}
