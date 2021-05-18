package com.weather.weatherdataapi.model.dto.responsedto.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoronaResponseDto {

    private LocalDate date;

    private Integer currentBigRegionNewCaseCount;

    private Integer allNewCaseCount;

}
