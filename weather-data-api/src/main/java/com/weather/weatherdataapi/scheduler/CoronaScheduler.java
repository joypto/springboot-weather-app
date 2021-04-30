package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.service.CoronaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class CoronaScheduler {

    private final CoronaService coronaService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void cronJobSch() {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date now = new Date();
            String strDate = sdf.format(now);

            log.info("{} SCHEDULER::Corona::OpenApi 호출을 통해 전일 코로나 확진자 정보를 갱신합니다.", strDate);

            coronaService.fetchAndStoreCoronaInfoUsingOpenApi();

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

}
