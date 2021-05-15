package com.weather.weatherdataapi.repository;

import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.model.entity.UserOftenSeenRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOftenSeenRegionRepository extends JpaRepository<UserOftenSeenRegion, Long> {

    List<UserOftenSeenRegion> findAllByUser(User user);

    List<UserOftenSeenRegion> findAllByUserOrderByCreatedAt(User user);

    void deleteAllByUser(User user);

}
