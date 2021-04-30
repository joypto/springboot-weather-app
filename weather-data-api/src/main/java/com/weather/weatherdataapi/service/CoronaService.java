package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.entity.Corona;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.CoronaRepository;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import com.weather.weatherdataapi.util.openapi.corona.gov.GovCoronaOpenApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoronaService {

    private final CoronaRepository coronaRepository;

    private final GovCoronaOpenApi govCoronaOpenApi;

    public void fetchAndStoreCoronaInfoUsingOpenApi() throws Exception {
        ICoronaInfo info = govCoronaOpenApi.getInfo();

        coronaRepository.deleteAll();

        for (int i = 0; i < info.getItemList().size(); i++) {
            ICoronaItem item = info.getItemList().get(i);

            Corona corona = new Corona(item);
            coronaRepository.save(corona);
        }

        log.info("fetchAndStoreCorona::코로나 데이터를 성공적으로 갱신하였습니다.");
    }

    public Corona getInfoByRegion(Region region) {
        return coronaRepository.findByBigRegion(region.getBigRegion()).orElseThrow(() -> new RuntimeException("해당하는 지역의 정보가 없습니다."));
    }

    public Corona getInfoByRegion(String bigRegion) {
        return coronaRepository.findByBigRegion(bigRegion).orElseThrow(() -> new RuntimeException("해당하는 지역의 정보가 없습니다."));
    }

    public Corona getTotalInfo() {
        return getInfoByRegion("합계");
    }
}
