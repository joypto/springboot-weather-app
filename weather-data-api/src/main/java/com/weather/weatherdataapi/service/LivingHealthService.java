package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.repository.info.InvalidLivingHealthInfoException;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.LivingHealthInfo;
import com.weather.weatherdataapi.model.vo.redis.LivingHealthRedisVO;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.info.LivingHealthInfoRepository;
import com.weather.weatherdataapi.repository.redis.LivingHealthRedisRepository;
import com.weather.weatherdataapi.util.HealthScoreUtil;
import com.weather.weatherdataapi.util.LivingScoreUtil;
import com.weather.weatherdataapi.util.openapi.living_health.LivingHealthApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LivingHealthService {

    private final LivingHealthInfoRepository livingHealthInfoRepository;
    private final LivingHealthRedisRepository livingHealthRedisRepository;
    private final BigRegionRepository bigRegionRepository;
    private final LivingHealthApi livingHealthApi;


    /**
     * 여기서부터는 매일 아침 6시에 생활보건기상지수를 업데이트하는 스케줄러에 쓰이는 메서드입니다.
     */
    @Transactional
    public void fetchAndStoreInfoUsingOpenApi() throws Exception {
        if (checkAlreadyHasLatestInfo() == true)
            return;

        livingHealthRedisRepository.deleteAll();

        List<BigRegion> bigRegionList = bigRegionRepository.findAll();


        for (int i = 0; i < 19; i++) {
            BigRegion bigRegion = bigRegionList.get(i);
            String admCode = bigRegionList.get(i).getAdmCode();
            LivingHealthInfo livingHealthInfo = livingHealthApi.livingHealthApi(bigRegion, admCode);

            LivingHealthInfo latestLivingHealthInfo = livingHealthInfoRepository.findFirstByOrderByCreatedAtDesc().get();
            if (livingHealthInfo.getDate().equals(latestLivingHealthInfo.getDate())) {
                log.info("00시 - 06시 사이의 요청입니다. 불러온 값을 DB에 저장하지 않습니다.");
                return;
            }
            livingHealthInfoRepository.save(livingHealthInfo);
            LivingHealthRedisVO livingHealthRedisVO = new LivingHealthRedisVO(livingHealthInfo);
            livingHealthRedisRepository.save(livingHealthRedisVO);
        }
        log.info("생활기상지수 데이터를 성공적으로 갱신하였습니다.");
    }

    private boolean checkAlreadyHasLatestInfo() {
        if (livingHealthInfoRepository.count() == 0)
            return false;

        LivingHealthInfo latestData = livingHealthInfoRepository.findFirstByOrderByCreatedAtDesc().orElseThrow(() -> new InvalidLivingHealthInfoException());
        LocalDate current = LocalDate.now();

        if (latestData == null)
            return false;

        return latestData.getCreatedAt().toLocalDate().isEqual(current);
    }

    /**
     * 정보 요청이 왔을 때, TotalDataService 로 생활보건기상지수 정보와 점수를 반환해주는 메서드입니다.
     */
    public void setInfoAndScore(BigRegion currentBigRegion, ScoreResultDto scoreResultDto, TotalDataResponseDto weatherDataResponseDto) {
        weatherDataResponseDto.setLivingHealthWeather(getInfoByBigRegion(currentBigRegion));
        convertInfoToScore(currentBigRegion, scoreResultDto);
    }

    /**
     * 여기서부터는 해당 지역의 생활보건기상지수 정보를 DB 에서 찾아서 반환해주는 메서드입니다.
     */
    public LivingHealthInfo getInfoByBigRegion(BigRegion bigRegion) {
        LivingHealthInfo livingHealthInfo;
        Optional<LivingHealthRedisVO> queriedLivingHealthRedisVO = livingHealthRedisRepository.findById(bigRegion.getAdmCode());

        if (queriedLivingHealthRedisVO.isPresent()) {
            log.info("생활보건기상지수 데이터를 캐시 데이터베이스에서 불러옵니다.");
            LivingHealthRedisVO livingHealthRedisVO = queriedLivingHealthRedisVO.get();
            livingHealthInfo = new LivingHealthInfo(livingHealthRedisVO, bigRegion);
        } else {
            log.info("생활보건기상지수 데이터를 MySql 데이터베이스에서 불러오고 캐시 데이터베이스에 저장합니다.");
            livingHealthInfo = livingHealthInfoRepository.findFirstByBigRegionOrderByCreatedAtDesc(bigRegion).orElseThrow(() -> new InvalidLivingHealthInfoException());
            LivingHealthRedisVO livingHealthRedisVO = new LivingHealthRedisVO(livingHealthInfo);
            livingHealthRedisRepository.save(livingHealthRedisVO);
        }

        return livingHealthInfo;
    }

    /**
     * 여기서부터는 생활보건기상지수의 점수 변환 관련 메서드입니다.
     */
    public ScoreResultDto convertInfoToScore(BigRegion bigRegion, ScoreResultDto scoreResultDto) {

        LivingHealthInfo livingHealthInfo = livingHealthInfoRepository.findFirstByBigRegionOrderByCreatedAtDesc(bigRegion).orElseThrow(() -> new InvalidLivingHealthInfoException());

        // 천식폐질환지수 점수변환
        List<Integer> asthmaInfoList = new ArrayList<>();
        asthmaInfoList.add(convertHealthInfoToScore(livingHealthInfo.getAsthmaToday()));
        asthmaInfoList.add(convertHealthInfoToScore(livingHealthInfo.getAsthmaTomorrow()));
        for (int i = 0; i < 5; i++) {
            asthmaInfoList.add(convertHealthInfoToScore(livingHealthInfo.getAsthmaTheDayAfterTomorrow()));
        }

        scoreResultDto.setAsthmaResult(asthmaInfoList);

        // 꽃가루지수 점수변환
        List<Integer> pollenRiskInfoList = new ArrayList<>();
        pollenRiskInfoList.add(convertHealthInfoToScore(livingHealthInfo.getOakPollenRiskToday()));
        pollenRiskInfoList.add(convertHealthInfoToScore(livingHealthInfo.getOakPollenRiskTomorrow()));
        for (int i = 0; i < 5; i++) {
            pollenRiskInfoList.add(convertHealthInfoToScore(livingHealthInfo.getOakPollenRiskTheDayAfterTomorrow()));
        }

        scoreResultDto.setPollenRiskResult(pollenRiskInfoList);

        // 식중독지수 점수변환
        List<Integer> foodPoisonInfoList = new ArrayList<>();
        foodPoisonInfoList.add(convertFoodPoisonInfoToScore(livingHealthInfo.getFoodPoisonToday()));
        foodPoisonInfoList.add(convertFoodPoisonInfoToScore(livingHealthInfo.getFoodPoisonTomorrow()));
        for (int i = 0; i < 5; i++) {
            foodPoisonInfoList.add(convertFoodPoisonInfoToScore(livingHealthInfo.getFoodPoisonTheDayAfterTomorrow()));
        }

        scoreResultDto.setFoodPoisonResult(foodPoisonInfoList);

        // 자외선지수 점수변환
        List<Integer> uvInfoList = new ArrayList<>();
        uvInfoList.add(convertUvInfoToScore(livingHealthInfo.getUvToday()));
        uvInfoList.add(convertUvInfoToScore(livingHealthInfo.getUvTomorrow()));
        for (int i = 0; i < 5; i++) {
            uvInfoList.add(convertUvInfoToScore(livingHealthInfo.getUvTheDayAfterTomorrow()));
        }

        scoreResultDto.setUvResult(uvInfoList);

        return scoreResultDto;
    }

    // 보건기상지수 중 식중독 지수 제외한 그 외 지수의 점수 변환
    public Integer convertHealthInfoToScore(String wthIdx) {
        Integer score = HealthScoreUtil.convertHealthWthIdxToScore(wthIdx);
        return score;
    }

    // 보건기상지수 중 식중독 지수의 점수 반환
    public Integer convertFoodPoisonInfoToScore(String wthIdx) {
        if (Integer.parseInt(wthIdx) <= 55) {
            return 100;
        } else if (Integer.parseInt(wthIdx) <= 70) {
            return 70;
        } else if (Integer.parseInt(wthIdx) <= 85) {
            return 40;
        }
        return 10;
    }

    // 생활기상지수 중 자외선 지수의 점수 변환
    public Integer convertUvInfoToScore(String wthIdx) {
        Integer score = LivingScoreUtil.convertHealthWthIdxToScore(wthIdx);
        return score;
    }

}
