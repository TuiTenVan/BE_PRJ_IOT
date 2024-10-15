package com.defty.iot.mapper;

import com.defty.iot.dto.request.UserRequest;
import com.defty.iot.dto.response.UserResponse;
import com.defty.iot.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {
    ModelMapper modelMapper;
    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }
    public User toUser(UserRequest userRequest) {
        return modelMapper.map(userRequest, User.class);
    }
}
