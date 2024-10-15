package com.defty.iot.service.impl;

import com.defty.iot.dto.request.UserRequest;
import com.defty.iot.dto.response.UserResponse;
import com.defty.iot.entity.User;
import com.defty.iot.mapper.UserMapper;
import com.defty.iot.repository.IUserRepository;
import com.defty.iot.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    IUserRepository userRepository;
    UserMapper userMapper;

    @Override
    public void createUser(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        userRepository.save(user);
    }

    @Override
    public Page<UserResponse> findUser(String name, String studentCode, Pageable pageable) {
        Page<User> userPage = userRepository.findUser(name, studentCode, pageable);
        return userPage.map(user -> userMapper.toUserResponse(user));
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setClassName(userRequest.getClassName());
        user.setStudentCode(userRequest.getStudentCode());
        user.setGender(userRequest.getGender());
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
