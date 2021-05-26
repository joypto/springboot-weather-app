package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.FailedFetchException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AirPollutionService {

    private final AirPollutionInfoRepository airPollutionRepository;
    private final AirPollutionRedisRepository airPollutionRedisRepository;

    private final AirKoreaAirPollutionApi airKoreaAirPollutionOpenApi;
    private final AirKoreaStationUtil airKoreaStationUtil;
    private final AirKoreaUtil airKoreaUtil;

    public void setInfoAndScore(SmallRegion smallRegion, ScoreResultDto scoreResultDto, TotalDataResponseDto weatherDataResponseDto) {
        AirPollutionInfo airPollutionInfo = getInfoBySmallRegion(smallRegion);

        if (airPollutionInfo.getDateTime() != null) {
            weatherDataResponseDto.setAirPollution(airPollutionInfo);

            convertInfoToScore(airPollutionInfo, scoreResultDto);
        }

    }

    public AirPollutionInfo getInfoBySmallRegion(SmallRegion smallRegion) {
        Optional<AirPollutionRedisVO> queriedAirPollutionRedisVO = airPollutionRedisRepository.findById(smallRegion.getAdmCode());

        // 캐시된 데이터가 있다면 캐시된 데이터를 우선적으로 사용합니다.
        if (queriedAirPollutionRedisVO.isPresent()) {
            AirPollutionRedisVO airPollutionRedisVO = queriedAirPollutionRedisVO.get();
            AirPollutionInfo cachedInfo = new AirPollutionInfo(airPollutionRedisVO, smallRegion);

            return cachedInfo;
        }
        // 그렇지 않다면 최신 정보를 가져옵니다.
        // 또한 최신 정보는 캐싱합니다.
        else {
            AirPollutionInfo latestInfo = getSyncedLatestInfo(smallRegion);

            AirPollutionRedisVO airPollutionRedisVO = new AirPollutionRedisVO(latestInfo);

            airPollutionRedisRepository.save(airPollutionRedisVO);

            return latestInfo;
        }

    }

    /**
     * 원격 서버에서 제공하는 최신 정보와 일치하는 정보 객체를 반환합니다.
     * DB에 최신 정보가 저장되어 있지 않다면 최신 정보를 DB에도 저장합니다.
     * 만약 원격 서버에서 제공받을 수 없다면 빈 객체(empty object)를 반환합니다.
     *
     * @return 일반적으로 원격 서버에서 제공되는 최신 정보를 반환합니다.
     * 만약 원격 서버에서 제공받을 수 없다면 빈 객체(empty object)를 반환합니다.
     */
    private AirPollutionInfo getSyncedLatestInfo(SmallRegion smallRegion) {
        AirKoreaAirPollutionItem fetchedItem;

        try {
            fetchedItem = fetchUsingOpenApi(smallRegion);
        }
        // 원격 서버에서 정보를 가져오는 데 실패한다면 빈 객체(empty object)를 반환합니다.
        catch (Exception e) {
            AirPollutionInfo emptyInfo = new AirPollutionInfo();
            emptyInfo.setSmallRegion(smallRegion);

            return emptyInfo;
        }


        // DB에 해당 지역에 대한 어떠한 대기오염 정보도 저장되어 있지 않거나,
        // 또는 최신 데이터가 아닐 경우 DB에 저장합니다.
        Optional<AirPollutionInfo> queriedExistedInfo = airPollutionRepository.findFirstBySmallRegionOrderByCreatedAtDesc(smallRegion);

        if (queriedExistedInfo.isPresent() == false
                || airKoreaUtil.checkLatestInfoAlreadyExists(queriedExistedInfo.get(), fetchedItem) == false) {

            AirPollutionInfo newInfo = new AirPollutionInfo(fetchedItem, smallRegion);
            airPollutionRepository.save(newInfo);

            return newInfo;
        }

        // DB에 이미 최신 정보가 저장되어 있는 경우, 기존의 값을 반환합니다.
        return queriedExistedInfo.get();
    }

    private AirKoreaAirPollutionItem fetchUsingOpenApi(SmallRegion smallRegion) {

        List<String> nearStationNameList = airKoreaStationUtil.getNearStationNameListByRegion(smallRegion);

        for (int i = 0; i < nearStationNameList.size(); i++) {
            String nearStationName = nearStationNameList.get(i);

            try {
                AirKoreaAirPollutionItem response = airKoreaAirPollutionOpenApi.getResponseByStationName(nearStationName);
                return response;
            } catch (Exception e) {
                log.error(e.getMessage());

                if (i < nearStationNameList.size() - 1) {
                    String nextNearStationName = nearStationNameList.get(i + 1);
                    log.error("그 다음으로 가까운 미세먼지 측정소에서 정보를 가져오기를 시도합니다. ((실패){} -> (다음 측정소명){})", nearStationName, nextNearStationName);
                }
                // 인접한 모든 미세먼지 측정소에서 정보를 가져오는 데 실패하였을 때 실행됩니다.
                else {
                    throw new FailedFetchException("인접한 모든 미세먼지 측정소에서 정보를 가져오는 데 실패하였습니다.");
                }
            }

        }

        // 모든 측정소가 올바른 정보를 반환하지 않은 경우, 임시 값을 응답합니다.
        return null;
    }

    public void convertInfoToScore(AirPollutionInfo airPollution, ScoreResultDto scoreResultDto) {
        final int PM10_LEVEL1 = 15;
        final int PM10_LEVEL2 = 30;
        final int PM10_LEVEL3 = 40;
        final int PM10_LEVEL4 = 50;
        final int PM10_LEVEL5 = 75;
        final int PM10_LEVEL6 = 100;
        final int PM10_LEVEL7 = 150;

        final int PM25_LEVEL1 = 8;
        final int PM25_LEVEL2 = 15;
        final int PM25_LEVEL3 = 20;
        final int PM25_LEVEL4 = 25;
        final int PM25_LEVEL5 = 37;
        final int PM25_LEVEL6 = 50;
        final int PM25_LEVEL7 = 75;

        int pm10Score = 0;
        int pm25Score = 0;

        Integer pm10Value = airPollution.getPm10Value();

        if (pm10Value != null) {
            if (pm10Value <= PM10_LEVEL1)
                pm10Score = 100;
            else if (pm10Value <= PM10_LEVEL2)
                pm10Score = 85;
            else if (pm10Value <= PM10_LEVEL3)
                pm10Score = 70;
            else if (pm10Value <= PM10_LEVEL4)
                pm10Score = 55;
            else if (pm10Value <= PM10_LEVEL5)
                pm10Score = 40;
            else if (pm10Value <= PM10_LEVEL6)
                pm10Score = 25;
            else if (pm10Value <= PM10_LEVEL7)
                pm10Score = 10;
            else
                pm10Score = 5;
        }

        Integer pm25Value = airPollution.getPm25Value();

        if (pm25Value != null) {
            if (pm25Value <= PM25_LEVEL1)
                pm25Score = 100;
            else if (pm25Value <= PM25_LEVEL2)
                pm25Score = 85;
            else if (pm25Value <= PM25_LEVEL3)
                pm25Score = 70;
            else if (pm25Value <= PM25_LEVEL4)
                pm25Score = 55;
            else if (pm25Value <= PM25_LEVEL5)
                pm25Score = 40;
            else if (pm25Value <= PM25_LEVEL6)
                pm25Score = 25;
            else if (pm25Value <= PM25_LEVEL7)
                pm25Score = 10;
            else
                pm25Score = 5;
        }

        scoreResultDto.setPm10Result(pm10Score);
        scoreResultDto.setPm25Result(pm25Score);
    }

    @Transactional
    public void refreshCache() {
        airPollutionRedisRepository.deleteAll();
    }

}
