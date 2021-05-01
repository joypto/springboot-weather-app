package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import com.weather.weatherdataapi.model.entity.Region;
import com.weather.weatherdataapi.repository.info.CoronaInfoRepository;
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

    private final CoronaInfoRepository coronaRepository;

    private final GovCoronaOpenApi govCoronaOpenApi;

    public void fetchAndStoreCoronaInfoUsingOpenApi() throws Exception {
        ICoronaInfo info = govCoronaOpenApi.getInfo();

        coronaRepository.deleteAll();

        for (int i = 0; i < info.getItemList().size(); i++) {
            ICoronaItem item = info.getItemList().get(i);

            CoronaInfo corona = new CoronaInfo(item);
            coronaRepository.save(corona);
        }

        log.info("fetchAndStoreCorona::코로나 데이터를 성공적으로 갱신하였습니다.");
    }

    public CoronaInfo getInfoByRegion(Region region) {
        return coronaRepository.findByBigRegion(region.getBigRegion()).orElseThrow(() -> new RuntimeException("해당하는 지역의 정보가 없습니다."));
    }

    public CoronaInfo getInfoByRegion(String bigRegion) {
        return coronaRepository.findByBigRegion(bigRegion).orElseThrow(() -> new RuntimeException("해당하는 지역의 정보가 없습니다."));
    }

    public CoronaInfo getTotalInfo() {
        return getInfoByRegion("합계");
    }

    public int calculateScore(CoronaInfo corona) {
        final int CORONA_LEVEL15 = 300;
        final int CORONA_LEVEL2 = 400;
        final int CORONA_LEVEL25 = 800;

        int allNewCaseCount = corona.getNewLocalCaseCount() + corona.getNewForeignCaseCount();

        // 코로나 단계별로 점수를 반환합니다.
        if (allNewCaseCount <= CORONA_LEVEL15)
            return 100;
        else if (allNewCaseCount <= CORONA_LEVEL2)
            return 70;
        else if (allNewCaseCount <= CORONA_LEVEL25)
            return 40;
        else
            return 10;
    }
}
