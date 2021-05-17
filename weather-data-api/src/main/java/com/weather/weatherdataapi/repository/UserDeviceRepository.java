package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
}
