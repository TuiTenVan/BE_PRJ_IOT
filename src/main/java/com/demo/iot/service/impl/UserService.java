package com.demo.iot.service.impl;

import com.demo.iot.dto.request.UserRequest;
import com.demo.iot.dto.response.UserResponse;
import com.demo.iot.entity.Device;
import com.demo.iot.entity.DeviceUser;
import com.demo.iot.entity.User;
import com.demo.iot.exception.AlreadyExitException;
import com.demo.iot.exception.NotFoundException;
import com.demo.iot.mapper.UserMapper;
import com.demo.iot.repository.IDeviceRepository;
import com.demo.iot.repository.IDeviceUseRepository;
import com.demo.iot.repository.IUserRepository;
import com.demo.iot.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    IUserRepository userRepository;
    IDeviceUseRepository deviceUseRepository;
    IDeviceRepository deviceRepository;
    UserMapper userMapper;

    @Override
    public void createRfid(String rfidCode, String deviceCode) {
        Optional<User> user = userRepository.findByRfidCode(rfidCode);
        Optional<Device> device = deviceRepository.findByCodeDevice(deviceCode);
        if(device.isEmpty()){
            throw new NotFoundException("Device not found");
        }
        if(user.isPresent()){
            Optional<DeviceUser> deviceUser = deviceUseRepository.findByUserAndDevice(user.get(), device.get());
            if(deviceUser.isPresent()){
                throw new AlreadyExitException("Device user already exists");
            }
            DeviceUser deviceUserEntity = DeviceUser.builder()
                    .device(device.get())
                    .user(user.get())
                    .build();
            deviceUseRepository.save(deviceUserEntity);
        }
        else{
            User userEntity = User.builder()
                    .rfidCode(rfidCode)
                    .build();
            userRepository.save(userEntity);
            DeviceUser deviceUserEntity = DeviceUser.builder()
                    .device(device.get())
                    .user(userEntity)
                    .build();
            deviceUseRepository.save(deviceUserEntity);
        }
    }

    @Override
    @Transactional
    public void deleteRfid(String rfidCode, String deviceCode) {
        Optional<User> user = userRepository.findByRfidCode(rfidCode);
        Optional<Device> device = deviceRepository.findByCodeDevice(deviceCode);
        if(user.isEmpty()){
            throw new NotFoundException("User not found");
        }
        if(device.isEmpty()){
            throw new NotFoundException("Device not found");
        }
        Optional<DeviceUser> deviceUser = deviceUseRepository.findByUserAndDevice(user.get(), device.get());
        if(deviceUser.isEmpty()){
            throw new NotFoundException("Device user not found");
        }
        deviceUseRepository.delete(deviceUser.get());
        List<DeviceUser> deviceUsers = deviceUseRepository.findByUser(user.get());
        if(deviceUsers.isEmpty()){
            userRepository.delete(user.get());
        }
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
        }else{
            userPage = userRepository.findUser(username, studentCode, pageable);
        }
        return userPage.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse findUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toUserResponse).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }

    @Override
    public UserResponse updateUser(Integer id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        if (userRepository.existsByUsernameAndIdNot(userRequest.getUsername(), id)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmailAndIdNot(userRequest.getEmail(), id)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByPhoneAndIdNot(userRequest.getPhone(), id)) {
            throw new IllegalArgumentException("Phone already exists");
        }
        if (userRepository.existsByStudentCodeAndIdNot(userRequest.getStudentCode(), id)) {
            throw new IllegalArgumentException("Student code already exists");
        }
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
