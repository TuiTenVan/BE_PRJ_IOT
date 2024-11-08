package com.demo.iot.repository;

import com.demo.iot.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    RolePermission findByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
    List<RolePermission> findAllByRoleIdIn(List<Integer> roleIds);
}
