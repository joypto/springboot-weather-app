package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.service.info.CoronaService;
import com.weather.weatherdataapi.util.DateTimeUtil;
import com.weather.weatherdataapi.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoronaScheduler {

    private final CoronaService coronaService;

    @Scheduled(cron = "0 12 * * * ?", zone = DateTimeUtil.ZONE_NAME_ASIA_SEOUL)
    public void cronJobSch() {
        try {
            log.info("전일 코로나 확진자 정보를 갱신합니다.");

            coronaService.tryFetchAndStoreInfoUsingOpenApi();

            log.info("성공적으로 갱신하였습니다.");
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("갱신하는데 실패하였습니다.");
        }

    }

}
