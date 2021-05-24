package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.AlreadyExistsLatestDataException;
import com.weather.weatherdataapi.exception.FailedFetchException;
import com.weather.weatherdataapi.exception.repository.info.InvalidCoronaInfoException;
import com.weather.weatherdataapi.exception.repository.redis.InvalidCoronaRedisVOException;
import com.weather.weatherdataapi.model.dto.ScoreResultDto;
import com.weather.weatherdataapi.model.dto.responsedto.TotalDataResponseDto;
import com.weather.weatherdataapi.model.dto.responsedto.info.CoronaResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.info.CoronaInfo;
import com.weather.weatherdataapi.model.vo.redis.CoronaRedisVO;
import com.weather.weatherdataapi.repository.info.CoronaInfoRepository;
import com.weather.weatherdataapi.repository.redis.CoronaRedisRepository;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaInfo;
import com.weather.weatherdataapi.util.openapi.corona.ICoronaItem;
import com.weather.weatherdataapi.util.openapi.corona.gov.GovCoronaApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class CoronaService {

    private final CoronaInfoRepository coronaRepository;
    private final CoronaRedisRepository coronaRedisRepository;
    private final RegionService regionService;

    private final GovCoronaApi govCoronaOpenApi;

    private Integer cachedAllNewCaseCount;

    public Integer getAllNewCaseCount() {
        if (cachedAllNewCaseCount == null)
            refreshCache();

        return cachedAllNewCaseCount;
    }

    public void setInfoAndScore(BigRegion currentBigRegion, ScoreResultDto scoreResultDto, TotalDataResponseDto totalDataResponseDto) {
        CoronaInfo coronaInfo = getInfoByBigRegion(currentBigRegion);
        Integer allNewCaseCount = getAllNewCaseCount();

        setInfo(totalDataResponseDto, coronaInfo, allNewCaseCount);

        convertInfoToScore(scoreResultDto, allNewCaseCount);
    }

    public CoronaInfo getInfoByBigRegion(BigRegion bigRegion) {
        CoronaInfo coronaInfo;

        // 캐시 데이터가 없다면 캐시를 갱신합니다.
        if (coronaRedisRepository.existsById(bigRegion.getAdmCode()) == false) {
            refreshCache();
        }

        CoronaRedisVO coronaRedisVO = coronaRedisRepository.findById(bigRegion.getAdmCode()).orElseThrow(() -> new InvalidCoronaRedisVOException());
        coronaInfo = new CoronaInfo(coronaRedisVO, bigRegion);

        return coronaInfo;
    }

    public void tryFetchAndStoreInfoUsingOpenApi() {
        try {
            log.info("원격 서버에서 제공하는 최신 코로나 정보를 DB에 동기화합니다.");
            long startTime = System.currentTimeMillis();

            fetchAndStoreInfoUsingOpenApi();

            long endTime = System.currentTimeMillis();
            float diffTimeSec = (endTime - startTime) / 1000f;
            log.info("동기화를 성공적으로 마쳤습니다. ({}sec)", diffTimeSec);
        } catch (AlreadyExistsLatestDataException e) {
            log.warn(e.getMessage());
            log.warn("원격 서버에서 제공하는 코로나 정보가 DB에 이미 저장되어 있습니다.");
        } catch (FailedFetchException e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("원격 서버에서 코로나 정보를 가져오는 데 실패하였습니다.");
        }

        refreshCache();
    }

    @Transactional
    public void fetchAndStoreInfoUsingOpenApi() throws AlreadyExistsLatestDataException, FailedFetchException {
        if (checkAlreadyHasLatestData() == true)
            return;

        ICoronaInfo info = govCoronaOpenApi.getInfo();

        if (info.getItemList().isEmpty())
            throw new FailedFetchException("가져온 아이템 리스트가 비어있습니다.");

        for (int i = 0; i < info.getItemList().size(); i++) {
            ICoronaItem item = info.getItemList().get(i);

            // 파싱해온 정보 중, 합계 정보는 저장하지 않습니다.
            if (item.getRegionName().equals("합계"))
                continue;

            CoronaInfo corona = new CoronaInfo(item, regionService);
            coronaRepository.save(corona);
        }

        log.info("코로나 데이터를 성공적으로 갱신하였습니다.");
    }

    private void setInfo(TotalDataResponseDto totalDataResponseDto, CoronaInfo coronaInfo, Integer allNewCaseCount) {
        Integer bigRegionNewCaseCount = coronaInfo.getNewLocalCaseCount() + coronaInfo.getNewForeignCaseCount();
        CoronaResponseDto coronaResponseDto = new CoronaResponseDto(coronaInfo.getDate(), bigRegionNewCaseCount, allNewCaseCount);

        totalDataResponseDto.setCorona(coronaResponseDto);
    }

    private void convertInfoToScore(ScoreResultDto scoreResultDto, Integer allNewCaseCount) {
        final int CORONA_LEVEL15 = 300;
        final int CORONA_LEVEL2 = 400;
        final int CORONA_LEVEL25 = 800;

        int score = 0;

        // 코로나 단계별로 점수를 반환합니다.
        if (allNewCaseCount <= CORONA_LEVEL15)
            score = 100;
        else if (allNewCaseCount <= CORONA_LEVEL2)
            score = 70;
        else if (allNewCaseCount <= CORONA_LEVEL25)
            score = 40;
        else
            score = 10;

        scoreResultDto.setCoronaResult(score);
    }

    @Transactional
    public void refreshCache() {
        Optional<CoronaInfo> queriedLatestOneInfo = coronaRepository.findFirstByOrderByCreatedAtDesc();

        if (queriedLatestOneInfo.isPresent()) {

            /* 모든 지역의 코로나 정보를 캐싱합니다. */
            List<CoronaInfo> latestInfoList = coronaRepository.findAllByDate(queriedLatestOneInfo.get().getDate());

            coronaRedisRepository.deleteAll();

            for (CoronaInfo info : latestInfoList) {
                CoronaRedisVO redisVO = new CoronaRedisVO(info);

                coronaRedisRepository.save(redisVO);
            }

            /* 모든 지역의 확진자수 총합을 계산하고, 캐싱합니다. */
            cachedAllNewCaseCount = null;

            AtomicInteger allNewCaseCount = new AtomicInteger();
            latestInfoList.forEach(coronaRedisVO -> {
                allNewCaseCount.addAndGet(coronaRedisVO.getNewLocalCaseCount() + coronaRedisVO.getNewForeignCaseCount());
            });

            cachedAllNewCaseCount = allNewCaseCount.get();

        }

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
