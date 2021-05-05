package com.weather.weatherdataapi.scheduler;

import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.repository.redis.AirPollutionRedisRepository;
import com.weather.weatherdataapi.service.AirPollutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AirPollutionScheduler {

    /**
     * 최신 여부를 검사하는 기준이 되는 지역 코드입니다.
     * 1171000000은 송파구 지역 코드입니다.
     */
    private static final String CRITERIA_REGION_ADM_CODE = "1171000000";

    private final AirPollutionService airPollutionService;
    private final AirPollutionRedisRepository airPollutionRedisRepository;

    private final BigRegionRepository bigRegionRepository;
    private final SmallRegionRepository smallRegionRepository;

    @Scheduled(cron = "0 10 * * * ?")
    public void cronJobSch() {
        try {
            log.info("cronJobSch::OpenApi 서버의 대기오염 정보가 갱신되었는지 확인합니다.");

            SmallRegion criteriaRegion = smallRegionRepository.findByAdmCode(CRITERIA_REGION_ADM_CODE);

            if (criteriaRegion == null) {
                throw new RuntimeException("cronJobSch::DB에 기준 지역 정보가 존재하지 않습니다.");
            }

            if (airPollutionService.checkLatestDataAlreadyExistsByRegion(criteriaRegion)) {
                log.info("cronJobSch::이미 최신데이터를 가지고 있습니다.");
            } else {
                log.info("cronJobSch::갱신되었음을 확인했습니다. 캐시 데이터를 삭제합니다.");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            log.error("cronJobSch::갱신하는데 실패하였습니다.");
        }

    }
}
