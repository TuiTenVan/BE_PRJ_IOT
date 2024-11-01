package com.demo.iot.service;

import com.demo.iot.dto.request.RoleRequest;
import com.demo.iot.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface IRoleService{
    Page<RoleResponse> getAllRoles(String name, Pageable pageable);
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse updateRole(Integer id, RoleRequest roleRequest);
    RoleResponse getRoleId(Integer roleId);
    RoleResponse assignPermissionToRole(Integer roleId, List<Integer> permissionIds);
    RoleResponse unassignPermissionFromRole(Integer roleId, List<Integer> permissionIds);
}
