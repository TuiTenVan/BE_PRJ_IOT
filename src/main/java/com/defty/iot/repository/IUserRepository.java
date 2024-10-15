package com.defty.iot.repository;

import com.defty.iot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE LOWER(u.username) " +
            "LIKE LOWER(CONCAT('%', :username, '%')) " +
            "OR LOWER(u.studentCode) " +
            "LIKE LOWER(CONCAT('%', :studentCode, '%'))")
    Page<User> findUser(@Param("username") String username, @Param("studentCode") String studentCode, Pageable pageable);
}
