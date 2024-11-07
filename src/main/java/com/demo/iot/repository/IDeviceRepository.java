package com.demo.iot.repository;

import com.demo.iot.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByName(String name);
    Optional<Device> findByLocation(String location);
    Optional<Device> findByCodeDevice(String codeDevice);   
}
