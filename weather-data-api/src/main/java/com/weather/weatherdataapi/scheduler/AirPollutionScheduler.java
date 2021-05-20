package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.service.AirPollutionService;
import com.weather.weatherdataapi.util.DateTimeUtil;
import com.weather.weatherdataapi.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AirPollutionScheduler {

    private final AirPollutionService airPollutionService;

    @Scheduled(cron = "0 10 * * * ?", zone = DateTimeUtil.ZONE_NAME_ASIA_SEOUL)
    public void cronJobSch() {
        try {
            log.info("캐시된 대기오염 정보를 삭제합니다.");

            airPollutionService.refreshCache();

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("캐시를 삭제하는데 실패하였습니다.");
        }

    }
}
