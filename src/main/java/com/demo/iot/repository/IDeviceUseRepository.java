package com.demo.iot.repository;

import com.demo.iot.entity.Device;
import com.demo.iot.entity.DeviceUser;
import com.demo.iot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDeviceUseRepository extends JpaRepository<DeviceUser, Integer> {
    Optional<DeviceUser> findByUserAndDevice(User user, Device device);
    List<DeviceUser> findByUser(User user);
}
