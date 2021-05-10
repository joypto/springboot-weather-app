package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.AlreadyExistsLatestDataException;
import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.exception.repository.info.InvalidCoronaInfoException;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import com.weather.weatherdataapi.model.vo.redis.CoronaRedisVO;
import com.weather.weatherdataapi.repository.info.CoronaInfoRepository;
import com.weather.weatherdataapi.repository.redis.CoronaRedisRepository;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import com.weather.weatherdataapi.util.openapi.corona.gov.GovCoronaApi;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoronaService {

    private final CoronaInfoRepository coronaRepository;
    private final CoronaRedisRepository coronaRedisRepository;
    private final RegionService regionService;

    private final GovCoronaApi govCoronaOpenApi;

    private final ReverseGeoCodingApi reverseGeoCodingApi;

    private Integer cachedAllNewCaseCount;

    // TODO: 의도대로 동작하지 않습니다.
    //  redisRepository에 현재 날짜의 모든 corona Info들이 저장되어 있어야 정상적으로 작동할 것입니다.
    public Integer getAllNewCaseCount() {
        // 캐시된 값이 없다면 계산한 뒤 캐싱합니다.
        if (cachedAllNewCaseCount == null) {
            Iterable<CoronaRedisVO> coronaInfoList = coronaRedisRepository.findAll();

            AtomicInteger allNewCaseCount = new AtomicInteger();
            coronaInfoList.forEach(coronaRedisVO -> {
                allNewCaseCount.addAndGet(coronaRedisVO.getNewLocalCaseCount() + coronaRedisVO.getNewForeignCaseCount());
            });

            cachedAllNewCaseCount = allNewCaseCount.get();
        }

        return cachedAllNewCaseCount;
    }

    private void clearAllNewCaseCountCache() {
        cachedAllNewCaseCount = null;
    }

    public void setInfoAndScore(BigRegion currentBigRegion, ScoreResultDto scoreResultDto, TotalDataResponseDto weatherDataResponseDto) {
        CoronaInfo coronaInfo = getInfoByBigRegion(currentBigRegion);

        weatherDataResponseDto.setCoronaCurrentBigRegionNewCaseCount(coronaInfo.getNewLocalCaseCount() + coronaInfo.getNewForeignCaseCount());

        weatherDataResponseDto.setCoronaAllNewCaseCount(getAllNewCaseCount());

        convertInfoToScore(scoreResultDto);
    }

    public CoronaInfo getInfoByBigRegion(BigRegion bigRegion) {
        CoronaInfo coronaInfo;

        // 캐시 데이터가 있다면 캐시 데이터를 먼저 사용합니다.
        if (coronaRedisRepository.existsById(bigRegion.getAdmCode())) {
            CoronaRedisVO coronaRedisVO = coronaRedisRepository.findById(bigRegion.getAdmCode()).get();
            coronaInfo = new CoronaInfo(coronaRedisVO, bigRegion);
        } else {
            coronaInfo = coronaRepository.findFirstByBigRegionOrderByCreatedAtDesc(bigRegion).orElseThrow(() -> new InvalidCoronaInfoException());
            CoronaRedisVO coronaRedisVO = new CoronaRedisVO(coronaInfo);
            coronaRedisRepository.save(coronaRedisVO);
        }

        return coronaInfo;
    }

    public void tryFetchAndStoreInfoUsingOpenApi() {
        try {
            fetchAndStoreInfoUsingOpenApi();
        } catch (AlreadyExistsLatestDataException e) {
            log.warn(e.getMessage());
            log.warn("run::원격 서버에서 제공하는 코로나 정보가 DB에 이미 저장되어 있습니다.");
        } catch (FailedFetchException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("run::원격 서버에서 코로나 정보를 가져오는 데 실패하였습니다.");
        }

    }

    @Transactional
    public void fetchAndStoreInfoUsingOpenApi() throws AlreadyExistsLatestDataException, FailedFetchException {
        if (checkAlreadyHasLatestData() == true)
            return;

        ICoronaInfo info = govCoronaOpenApi.getInfo();

        if (info.getItemList().isEmpty())
            throw new FailedFetchException("가져온 아이템 리스트가 비어있습니다.");

        coronaRedisRepository.deleteAll();
        clearAllNewCaseCountCache();

        for (int i = 0; i < info.getItemList().size(); i++) {
            ICoronaItem item = info.getItemList().get(i);

            // 파싱해온 정보 중, 합계 정보는 저장하지 않습니다.
            if (item.getRegionName().equals("합계"))
                continue;

            CoronaInfo corona = new CoronaInfo(item, regionService);
            coronaRepository.save(corona);

            CoronaRedisVO coronaRedisVO = new CoronaRedisVO(corona);
            coronaRedisRepository.save(coronaRedisVO);
        }

        log.info("코로나 데이터를 성공적으로 갱신하였습니다.");
    }

    public void convertInfoToScore(ScoreResultDto scoreResultDto) {
        final int CORONA_LEVEL15 = 300;
        final int CORONA_LEVEL2 = 400;
        final int CORONA_LEVEL25 = 800;

        int newCaseCount = getAllNewCaseCount();
        int score = 0;

        // 코로나 단계별로 점수를 반환합니다.
        if (newCaseCount <= CORONA_LEVEL15)
            score = 100;
        else if (newCaseCount <= CORONA_LEVEL2)
            score = 70;
        else if (newCaseCount <= CORONA_LEVEL25)
            score = 40;
        else
            score = 10;

        scoreResultDto.setCoronaResult(score);
    }

    private boolean checkAlreadyHasLatestData() {
        if (coronaRepository.count() == 0)
            return false;

        CoronaInfo latestData = coronaRepository.findFirstByOrderByCreatedAtDesc().orElseThrow(() -> new InvalidCoronaInfoException());
        LocalDate current = LocalDate.now();

        if (latestData == null)
            return false;

        return latestData.getDate().isEqual(current);
    }
}
