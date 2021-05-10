package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.repository.info.InvalidAirPollutionInfoException;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.entity.info.AirPollutionInfo;
import com.weather.weatherdataapi.model.vo.redis.AirPollutionRedisVO;
import com.weather.weatherdataapi.repository.info.AirPollutionInfoRepository;
import com.weather.weatherdataapi.repository.redis.AirPollutionRedisRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaStationUtil;
import com.weather.weatherdataapi.util.openapi.air_pollution.AirKoreaUtil;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea.AirKoreaAirPollutionItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AirPollutionService {

    private final AirPollutionInfoRepository airPollutionRepository;
    private final AirPollutionRedisRepository airPollutionRedisRepository;

    private final AirKoreaAirPollutionApi airKoreaAirPollutionOpenApi;
    private final AirKoreaStationApi airKoreaStationOpenApi;
    private final AirKoreaStationUtil airKoreaStationUtil;
    private final AirKoreaUtil airKoreaUtil;

    public void setInfoAndScore(SmallRegion smallRegion, ScoreResultDto scoreResultDto, TotalDataResponseDto weatherDataResponseDto) {
        AirPollutionInfo airPollutionInfo = getInfoBySmallRegion(smallRegion);

        weatherDataResponseDto.setAirPollution(airPollutionInfo);

        convertInfoToScore(airPollutionInfo, scoreResultDto);
    }

    public AirPollutionInfo getInfoBySmallRegion(SmallRegion smallRegion) {
        AirPollutionInfo airPollutionInfo;
        Optional<AirPollutionRedisVO> queriedAirPollutionRedisVO = airPollutionRedisRepository.findById(smallRegion.getAdmCode());

        // 캐시된 데이터가 있다면 캐시된 데이터를 우선적으로 사용합니다.
        if (queriedAirPollutionRedisVO.isPresent()) {
            AirPollutionRedisVO airPollutionRedisVO = queriedAirPollutionRedisVO.get();
            airPollutionInfo = new AirPollutionInfo(airPollutionRedisVO, smallRegion);
        }
        // 그렇지 않다면 원격 서버에서 최신 정보를 가져옵니다.
        // 가져온 최신 정보가 DB에 저장되어 있지 않다면, 이를 DB에 저장합니다.
        else {
            AirPollutionInfo existedInfo = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion).orElseThrow(() -> new InvalidAirPollutionInfoException());
            AirKoreaAirPollutionItem fetchedItem = fetchUsingOpenApi(smallRegion);

            // DB에 해당 지역에 대한 어떠한 대기오염 정보도 저장되어 있지 않거나,
            // 또는 최신 데이터가 아닐 경우
            // OpenApi로 가져온 정보를 DB에 저장한 후 사용합니다.
            if (existedInfo == null || airKoreaUtil.checkLatestInfoAlreadyExists(existedInfo, fetchedItem) == false) {
                airPollutionInfo = new AirPollutionInfo(fetchedItem, smallRegion);
                airPollutionRepository.save(airPollutionInfo);
            }
            // 그렇지 않은 경우 DB에 있는 데이터를 바로 사용합니다.
            else {
                airPollutionInfo = existedInfo;
            }

            AirPollutionRedisVO airPollutionRedisVO = new AirPollutionRedisVO(airPollutionInfo);
            airPollutionRedisRepository.save(airPollutionRedisVO);
        }

        return airPollutionInfo;
    }

    private AirKoreaAirPollutionItem fetchUsingOpenApi(SmallRegion smallRegion) {
        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        AirKoreaAirPollutionItem response = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);
        return response;
    }

    public void convertInfoToScore(AirPollutionInfo airPollution, ScoreResultDto scoreResultDto) {
        final int PM10_GOOD = 30;
        final int PM10_NORMAL = 80;
        final int PM10_BAD = 150;

        final int PM25_GOOD = 15;
        final int PM25_NORMAL = 35;
        final int PM25_BAD = 75;

        int pm10Score = 0;
        int pm25Score = 0;

        Integer pm10Value = airPollution.getPm10Value();

        if (pm10Value != null) {
            if (pm10Value <= PM10_GOOD)
                pm10Score = 100;
            else if (pm10Value <= PM10_NORMAL)
                pm10Score = 70;
            else if (pm10Value <= PM10_BAD)
                pm10Score = 40;
            else
                pm10Score = 10;
        }

        Integer pm20Value = airPollution.getPm25Value();

        if (pm20Value != null) {
            if (airPollution.getPm25Value() <= PM25_GOOD)
                pm25Score = 100;
            else if (airPollution.getPm25Value() <= PM25_NORMAL)
                pm25Score = 70;
            else if (airPollution.getPm25Value() <= PM25_BAD)
                pm25Score = 40;
            else
                pm25Score = 10;
        }

        scoreResultDto.setPm10Result(pm10Score);
        scoreResultDto.setPm25Result(pm25Score);
    }

    public boolean checkLatestDataAlreadyExistsByRegion(SmallRegion smallRegion) {
        AirPollutionInfo latestData = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion).orElseThrow(() -> new InvalidAirPollutionInfoException());

        String nearestStationName = airKoreaStationUtil.getNearestStationNameByRegion(smallRegion);
        AirKoreaAirPollutionItem latestFetchedData = airKoreaAirPollutionOpenApi.getResponseByStationName(nearestStationName);

        return airKoreaUtil.checkLatestInfoAlreadyExists(latestData, latestFetchedData);
    }

}
