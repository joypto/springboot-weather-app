package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.AlreadyExistsLatestDataException;
import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.exception.repository.info.InvalidLivingHealthInfoException;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import com.weather.weatherdataapi.model.vo.redis.LivingHealthRedisVO;
import com.weather.weatherdataapi.repository.info.LivingHealthInfoRepository;
import com.weather.weatherdataapi.repository.redis.LivingHealthRedisRepository;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.HealthScoreUtil;
import com.weather.weatherdataapi.util.LivingScoreUtil;
import com.weather.weatherdataapi.util.openapi.health.HealthApi;
import com.weather.weatherdataapi.util.openapi.health.asthma.AsthmaItem;
import com.weather.weatherdataapi.util.openapi.health.food_poison.FoodPoisonItem;
import com.weather.weatherdataapi.util.openapi.health.pollen_risk.PollenRiskItem;
import com.weather.weatherdataapi.util.openapi.living.LivingApi;
import com.weather.weatherdataapi.util.openapi.living.uv.UvItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LivingHealthServiceV2 {

    private final LivingHealthInfoRepository livingHealthInfoRepository;
    private final LivingHealthRedisRepository livingHealthRedisRepository;
    private final RegionService regionService;

    private final LivingApi livingApi;
    private final HealthApi healthApi;

    public void convertInfoToScore(BigRegion bigRegion, ScoreResultDto scoreResultDto) {
        LivingHealthInfo livingHealthInfo = livingHealthInfoRepository.findFirstByBigRegionOrderByCreatedAtDesc(bigRegion).orElseThrow(() -> new InvalidLivingHealthInfoException());

        List<Integer> uvInfoList = new ArrayList<>(7);
        List<Integer> asthmaInfoList = new ArrayList<>(7);
        List<Integer> pollenRiskInfoList = new ArrayList<>(7);
        List<Integer> foodPoisonInfoList = new ArrayList<>(7);

        uvInfoList.add(LivingScoreUtil.convertUvInfoToScore(Integer.parseInt(livingHealthInfo.getUvToday())));
        uvInfoList.add(LivingScoreUtil.convertUvInfoToScore(Integer.parseInt(livingHealthInfo.getUvTomorrow())));

        asthmaInfoList.add(HealthScoreUtil.convertHealthInfoToScore(Integer.parseInt(livingHealthInfo.getAsthmaToday())));
        asthmaInfoList.add(HealthScoreUtil.convertHealthInfoToScore(Integer.parseInt(livingHealthInfo.getAsthmaTomorrow())));

        pollenRiskInfoList.add(HealthScoreUtil.convertHealthInfoToScore(Integer.parseInt(livingHealthInfo.getOakPollenRiskToday())));
        pollenRiskInfoList.add(HealthScoreUtil.convertHealthInfoToScore(Integer.parseInt(livingHealthInfo.getOakPollenRiskTomorrow())));

        foodPoisonInfoList.add(HealthScoreUtil.convertFoodPoisonInfoToScore(Integer.parseInt(livingHealthInfo.getFoodPoisonToday())));
        foodPoisonInfoList.add(HealthScoreUtil.convertFoodPoisonInfoToScore(Integer.parseInt(livingHealthInfo.getFoodPoisonTomorrow())));

        // 3~7일 뒤의 예상 점수는 전부 3일 뒤의 예상 점수로 저장합니다.
        Integer uvTheDayAfterTomorrow = LivingScoreUtil.convertUvInfoToScore(Integer.parseInt(livingHealthInfo.getUvTheDayAfterTomorrow()));
        Integer asthmaTheDayAfterTomorrow = HealthScoreUtil.convertHealthInfoToScore(Integer.parseInt(livingHealthInfo.getAsthmaTheDayAfterTomorrow()));
        Integer pollenRiskTheDayAfterTomorrow = HealthScoreUtil.convertHealthInfoToScore(Integer.parseInt(livingHealthInfo.getOakPollenRiskTheDayAfterTomorrow()));
        Integer foodPoisonTheDayAfterTomorrow = HealthScoreUtil.convertFoodPoisonInfoToScore(Integer.parseInt(livingHealthInfo.getFoodPoisonTheDayAfterTomorrow()));

        for (int i = 0; i < 5; i++) {
            uvInfoList.add(uvTheDayAfterTomorrow);
            asthmaInfoList.add(asthmaTheDayAfterTomorrow);
            pollenRiskInfoList.add(pollenRiskTheDayAfterTomorrow);
            foodPoisonInfoList.add(foodPoisonTheDayAfterTomorrow);
        }

        scoreResultDto.setAsthmaResult(asthmaInfoList);
        scoreResultDto.setPollenRiskResult(pollenRiskInfoList);
        scoreResultDto.setFoodPoisonResult(foodPoisonInfoList);
        scoreResultDto.setUvResult(uvInfoList);

    }

    public LivingHealthInfo getInfoByBigRegion(BigRegion bigRegion) {
        LivingHealthInfo livingHealthInfo;

        // 캐시 데이터가 없다면 캐시를 갱신합니다.
        if (livingHealthRedisRepository.existsById(bigRegion.getAdmCode()) == false) {
            refreshCache();
        }

        LivingHealthRedisVO livingHealthRedisVO = livingHealthRedisRepository.findById(bigRegion.getAdmCode()).orElseThrow(() -> new InvalidLivingHealthInfoException());
        livingHealthInfo = new LivingHealthInfo(livingHealthRedisVO, bigRegion);

        return livingHealthInfo;
    }

    public void tryFetchAndStoreInfoUsingOpenApi() {
        try {
            fetchAndStoreInfoUsingOpenApi();
        } catch (AlreadyExistsLatestDataException e) {
            log.warn(e.getMessage());
            log.warn("원격 서버에서 제공하는 생활/보건기상지수 정보가 DB에 이미 저장되어 있습니다.");
        } catch (FailedFetchException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("원격 서버에서 생활/보건기상지수 정보를 가져오는 데 실패하였습니다.");
        }

    }

    @Transactional
    public void fetchAndStoreInfoUsingOpenApi() throws AlreadyExistsLatestDataException, FailedFetchException {

        LocalDateTime now = LocalDateTime.now();

        List<BigRegion> allValidBigRegionList = regionService.getAllValidBigRegionList();

        for (BigRegion bigRegion : allValidBigRegionList) {
            String admCode = bigRegion.getAdmCode();

            UvItem uvItem = livingApi.getResponse(admCode, now);
            AsthmaItem asthmaItem = healthApi.getAsthmaResponse(admCode, now);
            FoodPoisonItem foodPoisonItem = healthApi.getFoodPoisonResponse(admCode, now);
            PollenRiskItem pollenRiskItem = healthApi.getPollenRiskResponse(admCode, now);

            // FIXME: uv 정보의 시간으로 덮어쓰고 있다. 모든 정보의 시간이 일치하는지 확인해봐야 합니다.
            String fetchedItemTimeText = uvItem.getDate();

            if (checkAlreadyHasLatestData(bigRegion, fetchedItemTimeText))
                throw new AlreadyExistsLatestDataException();

            LivingHealthInfo livingHealthInfo = new LivingHealthInfo(bigRegion, uvItem.getDate(), uvItem, asthmaItem, foodPoisonItem, pollenRiskItem);
            livingHealthInfoRepository.save(livingHealthInfo);
        }

        refreshCache();

        log.info("생활/보건기상지수 데이터를 성공적으로 갱신하였습니다.");
    }

    private void refreshCache() {
        Optional<LivingHealthInfo> queriedLivingHealthInfo = livingHealthInfoRepository.findFirstByOrderByCreatedAtDesc();

        if (queriedLivingHealthInfo.isPresent()) {

            /* 모든 지역의 코로나 정보를 캐싱합니다. */
            List<LivingHealthInfo> latestInfoList = livingHealthInfoRepository.findAllByDate(queriedLivingHealthInfo.get().getDate());

            livingHealthRedisRepository.deleteAll();

            for (LivingHealthInfo info : latestInfoList) {
                LivingHealthRedisVO redisVO = new LivingHealthRedisVO(info);

                livingHealthRedisRepository.save(redisVO);
            }

        }

    }

    private boolean checkAlreadyHasLatestData(BigRegion bigRegion, String fetchedItemTimeText) {
        final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHH");

        Optional<LivingHealthInfo> queriedLatestInfo = livingHealthInfoRepository.findFirstByBigRegionOrderByCreatedAtDesc(bigRegion);
        if (queriedLatestInfo.isPresent() == false)
            return false;

        LocalDateTime latestItemTime = LocalDateTime.parse(queriedLatestInfo.get().getDate());
        LocalDateTime fetchedItemTime = LocalDateTime.parse(fetchedItemTimeText, DATETIME_FORMATTER);

        return fetchedItemTime.isAfter(latestItemTime);
    }

}
