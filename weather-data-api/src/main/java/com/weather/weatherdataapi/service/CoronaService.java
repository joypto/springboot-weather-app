package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.info.CoronaInfoRepository;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import com.weather.weatherdataapi.util.openapi.corona.gov.GovCoronaApi;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoronaService {

    private final CoronaInfoRepository coronaRepository;
    private final BigRegionRepository bigRegionRepository;

    private final GovCoronaApi govCoronaOpenApi;

    private final ReverseGeoCodingApi reverseGeoCodingApi;

    public CoronaInfo getLatestInfoByBigRegion(CoordinateDto coordinateDto) {
        try {
            ReverseGeocodingResponseDto reverseGeocodingResponseDto = reverseGeoCodingApi.reverseGeocoding(coordinateDto);

            BigRegion bigRegion = bigRegionRepository.findByBigRegionName(reverseGeocodingResponseDto.getBigRegion());
            return coronaRepository.findFirstByBigRegionOrderByCreatedAt(bigRegion);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    @Transactional
    public void fetchAndStoreCoronaInfoUsingOpenApi() throws Exception {
        if (checkAlreadyHasLatestData() == true)
            return;

        ICoronaInfo info = govCoronaOpenApi.getInfo();

        for (int i = 0; i < info.getItemList().size(); i++) {
            ICoronaItem item = info.getItemList().get(i);

            // 파싱해온 정보 중, 합계 정보는 저장하지 않습니다.
            if (item.getRegionName().equals("합계"))
                continue;

            CoronaInfo corona = new CoronaInfo(item, bigRegionRepository);
            coronaRepository.save(corona);
        }

        log.info("fetchAndStoreCorona::코로나 데이터를 성공적으로 갱신하였습니다.");
    }

    public CoronaInfo getInfoByBigRegion(BigRegion bigRegion) {
        return coronaRepository.findFirstByBigRegionOrderByCreatedAt(bigRegion);
    }

    public int getTotalNewCaseCount(LocalDate date) {
        List<CoronaInfo> coronaInfoList = coronaRepository.findAllByDate(date);
        int totalNewCaseCount = coronaInfoList.stream().mapToInt(coronaInfo -> coronaInfo.getNewLocalCaseCount() + coronaInfo.getNewForeignCaseCount()).sum();
        return totalNewCaseCount;
    }

    public int calculateScore(int newCaseCount) {
        final int CORONA_LEVEL15 = 300;
        final int CORONA_LEVEL2 = 400;
        final int CORONA_LEVEL25 = 800;

        // 코로나 단계별로 점수를 반환합니다.
        if (newCaseCount <= CORONA_LEVEL15)
            return 100;
        else if (newCaseCount <= CORONA_LEVEL2)
            return 70;
        else if (newCaseCount <= CORONA_LEVEL25)
            return 40;
        else
            return 10;
    }

    private boolean checkAlreadyHasLatestData() {
        CoronaInfo latestData = coronaRepository.findFirstByOrderByCreatedAt();
        LocalDate current = LocalDate.now();

        if (latestData == null)
            return false;

        return latestData.getDate().isEqual(current);
    }
}
