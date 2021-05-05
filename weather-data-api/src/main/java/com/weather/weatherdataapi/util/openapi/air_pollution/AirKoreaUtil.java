package com.weather.weatherdataapi.util.openapi.air_pollution;

import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AirKoreaUtil {

    /**
     * DB에 있는 값이 최신 데이터인지 판별합니다.
     *
     * @param existedInfo DB에 있는 가장 최신의 행입니다.
     * @param fetchedItem 방금 OpenApi로 가져온 정보입니다.
     * @return 이미 DB에 있는 데이터가 최신 데이터인 경우 true를 반환합니다.
     */
    public boolean checkLatestInfoAlreadyExists(AirPollutionInfo existedInfo, AirKoreaAirPollutionItem fetchedItem) {
        if (existedInfo == null)
            return false;

        // 데이터 갱신 여부는 시간 단위 이상의 값으로만 판별합니다.
        // 분 단위 이하의 값은 고려하지 않습니다.
        LocalDateTime latestDataTime = LocalDateTime.of(
                existedInfo.getDateTime().getYear(),
                existedInfo.getDateTime().getMonth(),
                existedInfo.getDateTime().getDayOfMonth(),
                existedInfo.getDateTime().getHour(),
                0,
                0,
                0
        );

        LocalDateTime latestFetchedDataTime = LocalDateTime.parse(fetchedItem.getDataTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        latestFetchedDataTime = LocalDateTime.of(
                latestFetchedDataTime.getYear(),
                latestFetchedDataTime.getMonth(),
                latestFetchedDataTime.getDayOfMonth(),
                latestFetchedDataTime.getHour(),
                0,
                0,
                0
        );

        if (latestDataTime.isBefore(latestFetchedDataTime))
            return false;

        return true;
    }
}
