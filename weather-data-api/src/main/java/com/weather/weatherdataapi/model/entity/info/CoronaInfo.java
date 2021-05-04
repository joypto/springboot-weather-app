package com.weather.weatherdataapi.model.entity.info;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.Timestamped;
import com.weather.weatherdataapi.model.vo.redis.CoronaRedisVO;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.util.RegionUtil;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CoronaInfo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "corona_id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "big_region_id")
    private BigRegion bigRegion;

    @Column
    private LocalDate date;

    @Column
    private Integer newLocalCaseCount;

    @Column
    private Integer newForeignCaseCount;

    public CoronaInfo(ICoronaItem item, BigRegionRepository bigRegionRepository) throws RuntimeException {
        String convertedFullName = RegionUtil.convertAliasToFullName(item.getRegionName());
        BigRegion bigRegion = bigRegionRepository.findByBigRegionName(convertedFullName);
        this.bigRegion = bigRegion;

        this.date = item.getDate();
        this.newLocalCaseCount = item.getNewLocalCaseCount();
        this.newForeignCaseCount = item.getNewForeignCaseCount();
    }

    public CoronaInfo(CoronaRedisVO coronaRedisVO, BigRegion bigRegion) {
        this.id = coronaRedisVO.getId();
        this.bigRegion = bigRegion;
        this.date = coronaRedisVO.getDate();
        this.newLocalCaseCount = coronaRedisVO.getNewLocalCaseCount();
        this.newForeignCaseCount = coronaRedisVO.getNewForeignCaseCount();
    }

}