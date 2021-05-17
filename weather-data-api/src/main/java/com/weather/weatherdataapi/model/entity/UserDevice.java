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
    private String identification;

    @Column
    private String deviceInfo;

    @Column
    private String requestRegion;

    public UserDevice(String identification, String requestRegion, String deviceInfo) {
        this.identification = identification;
        this.requestRegion = requestRegion;
        this.deviceInfo = deviceInfo;
    }

}
