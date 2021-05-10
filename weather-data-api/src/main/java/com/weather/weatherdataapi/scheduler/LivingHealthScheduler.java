package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.service.LivingHealthService;
import com.weather.weatherdataapi.util.DateTimeUtil;
import com.weather.weatherdataapi.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LivingHealthScheduler {

    private final LivingHealthService livingHealthService;

    @Scheduled(cron = "0 5 6 * * ?", zone = DateTimeUtil.ZONE_NAME_ASIA_SEOUL)
    public void cronJobSch() {
        try {
            log.info("전일 생활보건기상지수 정보를 갱신합니다.");

            livingHealthService.fetchAndStoreInfoUsingOpenApi();

            log.info("생활보건기상지수 정보를 성공적으로 갱신하였습니다.");

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("생활보건기상지수 정보를 갱신하는데 실패하였습니다.");
        }
    }

}
