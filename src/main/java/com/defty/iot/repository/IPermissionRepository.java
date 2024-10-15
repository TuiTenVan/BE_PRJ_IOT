package com.defty.iot.repository;

import com.defty.iot.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Integer> {
    @Query("SELECT p FROM Permission p " +
            "JOIN RolePermission rp ON p.id = rp.permission.id " +
            "WHERE rp.role.id = :roleId")
    Set<Permission> findPermissionsByRoleId(@Param("roleId") Integer roleId);

    void deleteByIdIn(List<Integer> permissionIds);
}
