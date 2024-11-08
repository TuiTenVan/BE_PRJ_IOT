package com.demo.iot.repository;

import com.demo.iot.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhone(String phone);

    @Query("SELECT a FROM Account a " +
            "WHERE (:username IS NULL OR LOWER(a.username) " +
            "LIKE LOWER(CONCAT('%', :username, '%'))) AND a.status = 1")
    Page<Account> findAccount(@Param("username") String username, Pageable pageable);

    @Query("SELECT a FROM Account a WHERE a.status = 1")
    Page<Account> findAllWithStatus(Pageable pageable);
}
