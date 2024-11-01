package com.demo.iot.service;

import com.demo.iot.dto.request.UserRequest;
import com.demo.iot.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    void createRfid(String rfidCode);
    void deleteRfid(String rfidCode);
    void createUser(UserRequest userRequest);
    Page<UserResponse> findUser(String name, String studentCode, Pageable pageable);
    UserResponse updateUser(Integer id, UserRequest userRequest);
    void deleteUser(Integer id);
}
