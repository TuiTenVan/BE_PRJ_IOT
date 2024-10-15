package com.defty.iot.service;

import com.defty.iot.dto.request.UserRequest;
import com.defty.iot.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    void createUser(UserRequest userRequest);
    Page<UserResponse> findUser(String name, String studentCode, Pageable pageable);
    UserResponse updateUser(Integer id, UserRequest userRequest);
    void deleteUser(Integer id);
}
