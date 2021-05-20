package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class UserDevice extends Timestamped {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_device_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "request_region_id")
    private SmallRegion requestRegion;

    @Column
    private String deviceInfo;

    public UserDevice(User user, SmallRegion requestRegion, String deviceInfo) {
        this.user = user;
        this.requestRegion = requestRegion;
        this.deviceInfo = deviceInfo;
    }

}
