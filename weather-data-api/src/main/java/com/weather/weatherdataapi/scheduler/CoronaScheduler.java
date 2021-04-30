package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.service.CoronaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoronaScheduler {

    private final CoronaService coronaService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void cronJobSch() {
        try {
            log.info("cronJobSch::전일 코로나 확진자 정보를 갱신합니다.");

            coronaService.fetchAndStoreCoronaInfoUsingOpenApi();

            log.info("cronJobSch::성공적으로 갱신하였습니다.");
        } catch (Exception e) {
            log.error("cronJobSch::갱신하는데 실패하였습니다.");
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

}
