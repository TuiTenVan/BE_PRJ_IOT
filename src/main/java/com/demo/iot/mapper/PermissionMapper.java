package com.demo.iot.mapper;

import com.demo.iot.dto.request.PermissionRequest;
import com.demo.iot.dto.response.PermissionResponse;
import com.demo.iot.entity.Permission;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class PermissionMapper {
    ModelMapper modelMapper;
    public Permission toPermission(PermissionRequest permissionRequest) {
        return modelMapper.map(permissionRequest, Permission.class);
    }
    public PermissionResponse toPermissionResponse(Permission permission) {
        return modelMapper.map(permission, PermissionResponse.class);
    }
}
