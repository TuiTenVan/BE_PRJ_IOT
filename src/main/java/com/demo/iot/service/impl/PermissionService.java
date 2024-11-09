package com.demo.iot.service.impl;

import com.demo.iot.dto.request.PermissionRequest;
import com.demo.iot.dto.response.PermissionResponse;
import com.demo.iot.entity.Permission;
import com.demo.iot.exception.AlreadyExitException;
import com.demo.iot.exception.NotFoundException;
import com.demo.iot.mapper.PermissionMapper;
import com.demo.iot.repository.IPermissionRepository;
import com.demo.iot.service.IPermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService {
    IPermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        Optional<Permission> permission = permissionRepository.findPermissionByName(permissionRequest.getName());
        PermissionResponse permissionResponse;
        if(permission.isEmpty()) {
            Permission permissionEntity = permissionMapper.toPermission(permissionRequest);
            permissionRepository.save(permissionEntity);
            permissionResponse = permissionMapper.toPermissionResponse(permissionEntity);
        }
        else{
            throw new AlreadyExitException("Permission name already exist");
        }
        return permissionResponse;
    }

    @Override
    public Page<PermissionResponse> getAllPermissions(String name, Pageable pageable) {
        Page<Permission> permissions;
        if (name != null && !name.isEmpty()) {
            permissions = permissionRepository.findPermission(name, pageable);
        } else {
            permissions = permissionRepository.findAll(pageable);
        }
        return permissions.map(permissionMapper::toPermissionResponse);
    }

    @Override
    @Transactional
    public void deletePermissions(List<Integer> permissionIds) {
        permissionRepository.deleteByIdIn(permissionIds);
    }

    @Override
    public void updatePermission(Integer permissionId, PermissionRequest permissionRequest) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(
                () -> new NotFoundException("Permission not found with id: " + permissionId)
        );
        permission.setName(permissionRequest.getName());
        permission.setDescription(permissionRequest.getDescription());
        permissionRepository.save(permission);
    }

    @Override
    public PermissionResponse getPermissionById(Integer permissionId) {
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(
                () -> new NotFoundException("Permission not found with id: " + permissionId)
        );
        return permissionMapper.toPermissionResponse(permission);
    }
}
