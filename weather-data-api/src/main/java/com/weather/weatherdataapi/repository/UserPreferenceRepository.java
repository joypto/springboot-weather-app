package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    UserPreference findByIdentification(String identification);
}
