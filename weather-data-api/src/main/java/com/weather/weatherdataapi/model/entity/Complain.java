package com.weather.weatherdataapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.dto.requestdto.ComplainReqeustDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Complain extends Timestamped {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complain_id")
    private Long id;

    @Column
    private String identification;

    @Column
    private String title;

    @Column
    private String contents;

    public Complain(ComplainReqeustDto complainReqeustDto, String identification) {
        this.identification = identification;
        this.title = complainReqeustDto.getTitle();
        this.contents = complainReqeustDto.getContents();
    }

}
