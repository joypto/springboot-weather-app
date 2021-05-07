package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdentification(String identification);
}
