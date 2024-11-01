package com.demo.iot.service.impl;

import com.demo.iot.dto.request.UserRequest;
import com.demo.iot.dto.response.UserResponse;
import com.demo.iot.entity.User;
import com.demo.iot.mapper.UserMapper;
import com.demo.iot.repository.IUserRepository;
import com.demo.iot.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    IUserRepository userRepository;
    UserMapper userMapper;

    @Override
    public void createRfid(String rfidCode) {
        Optional<User> user = userRepository.findByRfidCode(rfidCode);
        if (user.isEmpty()) {
            User userEntity = User.builder().rfidCode(rfidCode).build();
            userRepository.save(userEntity);
        }
    }

    @Override
    @Transactional
    public void deleteRfid(String rfidCode) {
        Optional<User> user = userRepository.findByRfidCode(rfidCode);
        user.ifPresent(userRepository::delete);
    }

    @Override
    public void createUser(UserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        userRepository.save(user);
    }

    @Override
    public Page<UserResponse> findUser(String username, String studentCode, Pageable pageable) {
        Page<User> userPage;
        if(username == null && studentCode == null) {
            userPage = userRepository.findAll(pageable);
        }
        else{
            userPage = userRepository.findUser(username, studentCode, pageable);
        }
        return userPage.map(userMapper::toUserResponse);
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
    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
