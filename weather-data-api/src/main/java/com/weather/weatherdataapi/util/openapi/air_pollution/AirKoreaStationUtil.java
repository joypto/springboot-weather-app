package com.weather.weatherdataapi.util.openapi.air_pollution;

import com.weather.weatherdataapi.model.entity.region.Region;
import com.weather.weatherdataapi.repository.region.RegionRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationItem;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationOpenApi;
import com.weather.weatherdataapi.util.openapi.geo.kakao.KakaoGeoOpenApi;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AirKoreaStationUtil {

    private final AirKoreaStationOpenApi airKoreaStationOpenApi;
    private final KakaoGeoOpenApi kakaoGeoOpenApi;
    private final RegionRepository regionRepository;

    private Dictionary<String, Dictionary<String, String>> regionNameStationNameDict;

    @PostConstruct
    private void InitializeRegionStationNameDict() {
        log.info("InitializeRegionStationNameDict::각 지역에 대응되는 미세먼지 측정소 매핑을 시작합니다.");
        long startTime = System.currentTimeMillis();

        try {
            regionNameStationNameDict = new Hashtable<>();

            // 각 region에 가장 가까운 미세먼지 측정소의 이름을 저장합니다.
            List<Region> allRegionList = regionRepository.findAll();
            for (Region region : allRegionList) {
                KakaoGeoTranscoordResponseDocument transcoord = kakaoGeoOpenApi.convertWGS84ToWTM(region.getLongitude(), region.getLatitude());
                AirKoreaStationItem airKoreaStationItem = airKoreaStationOpenApi.getResponseItem(transcoord.getX(), transcoord.getY()).orElseThrow(() -> new RuntimeException());
                String nearestStationName = airKoreaStationItem.getStationName();

                // BigRegion에 대응되는 SmallRegion을 담을 HashTable이 없다면, 인스턴스를 생성합니다.
                if (regionNameStationNameDict.get(region.getBigRegion()) == null)
                    regionNameStationNameDict.put(region.getBigRegion(), new Hashtable<>());

                regionNameStationNameDict.get(region.getBigRegion()).put(region.getSmallRegion(), nearestStationName);
            }

            long endTime = System.currentTimeMillis();
            float diffTimeSec = (endTime - startTime) / 1000f;
            log.info("InitializeRegionStationNameDict::성공적으로 매핑했습니다. ({}sec)", diffTimeSec);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            log.info("InitializeRegionStationNameDict::매핑에 실패하였습니다.");
        }
    }

    public String getNearestStationNameByRegion(Region region) {
        return regionNameStationNameDict.get(region.getBigRegion()).get(region.getSmallRegion());
    }

    public String getNearestStationNameByRegionName(String bigRegion, String smallRegion) {
        return regionNameStationNameDict.get(bigRegion).get(smallRegion);
    }

}
