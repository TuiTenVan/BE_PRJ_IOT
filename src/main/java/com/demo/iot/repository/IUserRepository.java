package com.demo.iot.repository;

import com.demo.iot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE LOWER(u.username) " +
            "LIKE LOWER(CONCAT('%', :username, '%')) " +
            "OR LOWER(u.employeeCode) " +
            "LIKE LOWER(CONCAT('%', :employeeCode, '%'))")
    Page<User> findUser(@Param("username") String username, @Param("employeeCode") String employeeCode, Pageable pageable);

    Optional<User> findByRfidCode(String username);
    Optional<User> findByEmployeeCode(String employeeCode);

    boolean existsByUsernameAndIdNot(String username, Integer id);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsByPhoneAndIdNot(String phone, Integer id);
    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Integer id);

}
