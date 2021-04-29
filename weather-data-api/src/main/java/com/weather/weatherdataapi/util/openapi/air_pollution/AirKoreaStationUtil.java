package com.weather.weatherdataapi.util.openapi.air_pollution;

import com.weather.weatherdataapi.model.entity.AirPollutionStation;
import com.weather.weatherdataapi.model.entity.region.Region;
import com.weather.weatherdataapi.repository.AirPollutionStationRepository;
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
    private final AirPollutionStationRepository airPollutionStationRepository;
    private final RegionRepository regionRepository;

    private Dictionary<String, Dictionary<String, String>> regionNameStationNameDict;

    @PostConstruct
    private void InitializeRegionStationNameDict() {
        log.info("InitializeRegionStationNameDict::각 지역에 대응되는 미세먼지 측정소 매핑을 시작합니다.");
        long startTime = System.currentTimeMillis();

        try {
            regionNameStationNameDict = new Hashtable<>();

            List<Region> allRegionList = regionRepository.findAll();

            // 이미 DB에 측정소 정보가 저장되어 있다면, DB에 저장되어 있는 정보를 그대로 가져다 사용하면 됩니다.
            if (allRegionList.size() == airPollutionStationRepository.count()) {
                log.info("InitializeRegionStationNameDict::모든 지역에 매핑된 미세먼지 측정소 정보가 DB에 이미 존재합니다. DB의 데이터를 불러옵니다.");

                for (Region region : allRegionList) {
                    AirPollutionStation station = airPollutionStationRepository.findByRegion(region).orElseThrow(() -> new RuntimeException("해당 지역에 매핑된 측정소 정보가 없습니다."));
                    String nearestStationName = station.getStationName();

                    // BigRegion에 대응되는 SmallRegion을 담을 HashTable이 없다면, 인스턴스를 생성합니다.
                    if (regionNameStationNameDict.get(region.getBigRegion()) == null)
                        regionNameStationNameDict.put(region.getBigRegion(), new Hashtable<>());

                    regionNameStationNameDict.get(region.getBigRegion()).put(region.getSmallRegion(), nearestStationName);
                }
            }
            // 그렇지 않다면 OpenApi를 사용하여 측정소 정보를 매핑해야 합니다.
            // ** 주의: 아래 구문을 하루 내에 반복적으로 여러 차례 실행할 경우, 측정소 정보를 가져오는 OpenApi 호출 제한 횟수를 넘겨 더 이상 올바르게 작동하지 않을 수 있습니다.  **
            else {
                // 각 region에 가장 가까운 미세먼지 측정소의 이름을 저장합니다.
                for (Region region : allRegionList) {
                    KakaoGeoTranscoordResponseDocument transcoord = kakaoGeoOpenApi.convertWGS84ToWTM(region.getLongitude(), region.getLatitude());
                    AirKoreaStationItem airKoreaStationItem = airKoreaStationOpenApi.getResponseItem(transcoord.getX(), transcoord.getY()).orElseThrow(() -> new RuntimeException());
                    String nearestStationName = airKoreaStationItem.getStationName();

                    // BigRegion에 대응되는 SmallRegion을 담을 HashTable이 없다면, 인스턴스를 생성합니다.
                    if (regionNameStationNameDict.get(region.getBigRegion()) == null)
                        regionNameStationNameDict.put(region.getBigRegion(), new Hashtable<>());

                    regionNameStationNameDict.get(region.getBigRegion()).put(region.getSmallRegion(), nearestStationName);
                }
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
