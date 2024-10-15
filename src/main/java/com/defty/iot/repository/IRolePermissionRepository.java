package com.defty.iot.repository;

import com.defty.iot.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    RolePermission findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
}
