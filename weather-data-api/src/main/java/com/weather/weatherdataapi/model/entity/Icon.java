package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Icon {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "icon_id")
    private Long id;

    @Column
    private String dayIcon;

    @Column
    private String nightIcon;

    @Column
    private String description;

    @ElementCollection
    private List<String> message;

    public Icon(String dayIcon, String nightIcon, String description) {
        this.dayIcon = dayIcon;
        this.nightIcon = nightIcon;
        this.description = description;
    }

}
