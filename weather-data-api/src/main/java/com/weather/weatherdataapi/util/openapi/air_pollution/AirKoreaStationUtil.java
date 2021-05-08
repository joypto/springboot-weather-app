package com.weather.weatherdataapi.util.openapi.air_pollution;

import com.weather.weatherdataapi.model.entity.AirPollutionStation;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.repository.AirPollutionStationRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationApi;
import com.weather.weatherdataapi.util.openapi.air_pollution.airkorea_station.AirKoreaStationItem;
import com.weather.weatherdataapi.util.openapi.geo.kakao.KakaoGeoApi;
import com.weather.weatherdataapi.util.openapi.geo.kakao.transcoord.KakaoGeoTranscoordResponseDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AirKoreaStationUtil {

    private final AirKoreaStationApi airKoreaStationOpenApi;
    private final KakaoGeoApi kakaoGeoOpenApi;
    private final AirPollutionStationRepository airPollutionStationRepository;
    private final SmallRegionRepository smallRegionRepository;

    private Dictionary<String, Dictionary<String, String>> regionNameStationNameDict;

    public void initializeRegionStationNameDict() {
        log.info("각 지역에 대응되는 미세먼지 측정소 매핑을 시작합니다.");
        long startTime = System.currentTimeMillis();

        try {
            regionNameStationNameDict = new Hashtable<>();

            List<SmallRegion> allSmallRegionList = smallRegionRepository.findAll();

            // 이미 DB에 측정소 정보가 저장되어 있다면, DB에 저장되어 있는 정보를 그대로 가져다 사용하면 됩니다.
            if (allSmallRegionList.size() == airPollutionStationRepository.count()) {
                log.info("모든 지역에 매핑된 미세먼지 측정소 정보가 DB에 이미 존재합니다. DB의 데이터를 불러옵니다.");

                for (SmallRegion smallRegion : allSmallRegionList) {
                    AirPollutionStation station = airPollutionStationRepository.findBySmallRegion(smallRegion).orElseThrow(() -> new RuntimeException("해당 지역에 매핑된 측정소 정보가 없습니다."));
                    String nearestStationName = station.getStationName();

                    // BigRegion에 대응되는 SmallRegion을 담을 HashTable이 없다면, 인스턴스를 생성합니다.
                    String bigRegionName = smallRegion.getBigRegion().getBigRegionName();
                    if (regionNameStationNameDict.get(bigRegionName) == null)
                        regionNameStationNameDict.put(bigRegionName, new Hashtable<>());

                    regionNameStationNameDict.get(bigRegionName).put(smallRegion.getSmallRegionName(), nearestStationName);
                }
            }
            // 그렇지 않다면 OpenApi를 사용하여 측정소 정보를 매핑해야 합니다.
            // ** 주의: 아래 구문을 하루 내에 반복적으로 여러 차례 실행할 경우, 측정소 정보를 가져오는 OpenApi 호출 제한 횟수를 넘겨 더 이상 올바르게 작동하지 않을 수 있습니다.  **
            else {

                // 일부분이 이미 저장되어 있던 상태라면, 테이블을 초기화한 뒤 매핑을 시작합니다.
                if (allSmallRegionList.size() != 0) {
                    log.info("DB에 존재하는 매핑된 미세먼지 측정소 정보의 수가 모든 지역의 수와 다릅니다. 테이블을 초기화하고 다시 매핑합니다.");
                    airPollutionStationRepository.deleteAll();
                }

                // 각 region에 가장 가까운 미세먼지 측정소의 이름을 저장합니다.
                for (SmallRegion smallRegion : allSmallRegionList) {
                    KakaoGeoTranscoordResponseDocument transcoord = kakaoGeoOpenApi.convertWGS84ToWTM(smallRegion.getLongitude(), smallRegion.getLatitude());
                    AirKoreaStationItem airKoreaStationItem = airKoreaStationOpenApi.getResponseItem(transcoord.getX(), transcoord.getY());
                    String nearestStationName = airKoreaStationItem.getStationName();

                    // BigRegion에 대응되는 SmallRegion을 담을 HashTable이 없다면, 인스턴스를 생성합니다.
                    String bigRegionName = smallRegion.getBigRegion().getBigRegionName();

                    if (regionNameStationNameDict.get(bigRegionName) == null)
                        regionNameStationNameDict.put(bigRegionName, new Hashtable<>());

                    regionNameStationNameDict.get(bigRegionName).put(smallRegion.getSmallRegionName(), nearestStationName);

                    AirPollutionStation newStation = new AirPollutionStation(smallRegion, nearestStationName);
                    airPollutionStationRepository.save(newStation);
                }
            }

            long endTime = System.currentTimeMillis();
            float diffTimeSec = (endTime - startTime) / 1000f;
            log.info("성공적으로 매핑했습니다. ({}sec)", diffTimeSec);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            log.info("매핑에 실패하였습니다.");
        }
    }

    public String getNearestStationNameByRegion(SmallRegion region) {
        return regionNameStationNameDict.get(region.getBigRegion().getBigRegionName()).get(region.getSmallRegionName());
    }

    public String getNearestStationNameByRegionName(String bigRegion, String smallRegion) {
        return regionNameStationNameDict.get(bigRegion).get(smallRegion);
    }

}
