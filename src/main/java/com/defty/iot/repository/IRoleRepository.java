package com.defty.iot.repository;

import com.defty.iot.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Integer> {
    Role findRoleByName(String roleName);
}
