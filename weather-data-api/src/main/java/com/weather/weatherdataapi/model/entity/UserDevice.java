package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class UserDevice extends Timestamped{

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_device_id")
    private Long id;

    @Column
    private String deviceInfo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserDevice(String deviceInfo, User user) {
        this.deviceInfo = deviceInfo;
        this.user = user;
    }

    public UserDevice(String deviceInfo) {
        this.deviceInfo = deviceInfo;
        this.user = null;
    }

}
