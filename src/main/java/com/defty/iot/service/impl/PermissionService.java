package com.defty.iot.service.impl;

import com.defty.iot.dto.request.PermissionRequest;
import com.defty.iot.dto.response.PermissionResponse;
import com.defty.iot.entity.Permission;
import com.defty.iot.mapper.PermissionMapper;
import com.defty.iot.repository.IPermissionRepository;
import com.defty.iot.service.IPermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService {
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    @Override
    @Transactional
    public void deletePermissions(List<Integer> permissionIds) {
        permissionRepository.deleteByIdIn(permissionIds);
    }

    @Override
    public void updatePermission(Integer permissionId, PermissionRequest permissionRequest) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(
                () -> new RuntimeException("Permission not found with id: " + permissionId)
        );
        permission.setName(permissionRequest.getName());
        permission.setDescription(permissionRequest.getDescription());
        permissionRepository.save(permission);
    }
}
