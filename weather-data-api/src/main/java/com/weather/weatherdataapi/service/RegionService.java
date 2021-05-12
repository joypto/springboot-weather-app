package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.exception.repository.InvalidBigRegionException;
import com.weather.weatherdataapi.exception.repository.InvalidSmallRegionException;
import com.weather.weatherdataapi.model.dto.CoordinateDto;
import com.weather.weatherdataapi.model.dto.RegionDto;
import com.weather.weatherdataapi.model.dto.responsedto.ReverseGeocodingResponseDto;
import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.vo.csv.RegionCsvVO;
import com.weather.weatherdataapi.model.vo.redis.BigRegionRedisVO;
import com.weather.weatherdataapi.model.vo.redis.SmallRegionRedisVO;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.repository.redis.BigRegionRedisRepository;
import com.weather.weatherdataapi.repository.redis.SmallRegionRedisRepository;
import com.weather.weatherdataapi.util.CsvParserUtil;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.openapi.geo.naver.ReverseGeoCodingApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegionService {

    private final BigRegionRepository bigRegionRepository;
    private final SmallRegionRepository smallRegionRepository;
    private final BigRegionRedisRepository bigRegionRedisRepository;
    private final SmallRegionRedisRepository smallRegionRedisRepository;

    private final ReverseGeoCodingApi reverseGeoCodingApi;

    private Dictionary<String, String> bigRegionAdmCodeDict;
    private Dictionary<String, Dictionary<String, String>> smallRegionAdmCodeDict;

    public RegionDto getRegionName(CoordinateDto coordinateDto) {
        // 해당 위경도로 시/구 주소 문자열 반환
        ReverseGeocodingResponseDto address = reverseGeoCodingApi.reverseGeocoding(coordinateDto);
        return new RegionDto(address.getBigRegion(), address.getSmallRegion());
    }

    public BigRegion getBigRegionByName(String bigRegionName) {
        String admCode = getBigRegionAdmCodeByName(bigRegionName);

        BigRegionRedisVO bigRegionRedisVO = null;
        Optional<BigRegionRedisVO> queriedBigRegionRedisVO = bigRegionRedisRepository.findById(admCode);

        // 캐시된 데이터가 있다면 캐시된 데이터를 우선적으로 사용합니다.
        if (queriedBigRegionRedisVO.isPresent() == true) {
            bigRegionRedisVO = queriedBigRegionRedisVO.get();
            BigRegion bigRegion = new BigRegion(bigRegionRedisVO);

            return bigRegion;
        }
        // 그렇지 않다면 DB에서 조회한 데이터를 사용합니다.
        // 조회한 데이터는 이제부터 캐시됩니다.
        else {
            BigRegion bigRegion = bigRegionRepository.findByBigRegionName(bigRegionName).orElseThrow(() -> new InvalidBigRegionException());
            bigRegionRedisVO = new BigRegionRedisVO(bigRegion);
            bigRegionRedisRepository.save(bigRegionRedisVO);

            return bigRegion;
        }

    }

    public SmallRegion getSmallRegionByName(String bigRegionName, String smallRegionName) {
        String admCode = getSmallRegionAdmCodeByName(bigRegionName, smallRegionName);
        BigRegion bigRegion = getBigRegionByName(bigRegionName);

        SmallRegionRedisVO smallRegionRedisVO = null;
        Optional<SmallRegionRedisVO> queriedSmallRegionRedisVO = smallRegionRedisRepository.findById(admCode);

        // 캐시된 데이터가 있다면 캐시된 데이터를 우선적으로 사용합니다.
        if (queriedSmallRegionRedisVO.isPresent() == true) {
            smallRegionRedisVO = queriedSmallRegionRedisVO.get();
            SmallRegion smallRegion = new SmallRegion(smallRegionRedisVO, bigRegion);

            return smallRegion;
        }
        // 그렇지 않다면 DB에서 조회한 데이터를 사용합니다.
        // 조회한 데이터는 이제부터 캐시됩니다.
        else {
            SmallRegion smallRegion = smallRegionRepository.findByBigRegionAndSmallRegionName(bigRegion, smallRegionName).orElseThrow(() -> new InvalidSmallRegionException());
            smallRegionRedisVO = new SmallRegionRedisVO(smallRegion);
            smallRegionRedisRepository.save(smallRegionRedisVO);

            return smallRegion;
        }
    }

    private String getBigRegionAdmCodeByName(String bigRegionName) {
        return bigRegionAdmCodeDict.get(bigRegionName);
    }

    private String getSmallRegionAdmCodeByName(String bigRegionName, String smallRegionName) {
        return smallRegionAdmCodeDict.get(bigRegionName).get(smallRegionName);
    }

    public void initialize() {
        try {
            log.info("resources/data/region.csv 파일을 읽어 지역정보를 초기화합니다.");
            long startTime = System.currentTimeMillis();

            ClassPathResource regionCsvResource = new ClassPathResource("data/region.csv");
            List<RegionCsvVO> regionList = CsvParserUtil.parseCsvToObject(RegionCsvVO.class, regionCsvResource.getInputStream(), RegionCsvVO.getSchema());

            tryInitializeRegionTable(regionList);

            List<BigRegion> allBigRegionList = bigRegionRepository.findAll();
            List<SmallRegion> allSmallRegionList = smallRegionRepository.findAll();

            initializeRegionAdmCodeDict(allBigRegionList, allSmallRegionList);

            initializeRedisRepository(allBigRegionList, allSmallRegionList);

            long endTime = System.currentTimeMillis();
            float diffTimeSec = (endTime - startTime) / 1000f;
            log.info("초기화를 성공적으로 마쳤습니다. ({}sec)", diffTimeSec);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("초기화하는 도중 문제가 발생하였습니다.");
        }
    }

    private void tryInitializeRegionTable(List<RegionCsvVO> regionList) {
        if (checkRegionTableInitialized(regionList) == true) {
            log.info("모든 지역정보가 이미 DB에 존재합니다.");
            return;
        }

        initializeRegionTable(regionList);
    }

    @Transactional
    public void initializeRegionTable(List<RegionCsvVO> regionList) {

        BigRegion latestBigRegion = null;

        for (RegionCsvVO regionVO : regionList) {
            // BigRegion 에 대한 정보입니다.
            // BigRegion 정보에 대한 행은 smallRegion 필드의 값이 비어있는 행입니다.
            if (regionVO.isBigRegionInfo() == true) {

                // 이미 존재한다면 추가하지 않습니다.
                if (bigRegionRepository.findByAdmCode(regionVO.getAdmCode()).isPresent() == true) {
                    continue;
                }

                BigRegion newBigRegion = new BigRegion(regionVO.getBigRegion(), regionVO.getAdmCode(), regionVO.getLongitude(), regionVO.getLatitude());
                bigRegionRepository.save(newBigRegion);

                latestBigRegion = newBigRegion;
            }
            // 그 외에는 전부 소분류 지역에 대한 정보입니다.
            else {

                // 이미 존재한다면 추가하지 않습니다.
                if (smallRegionRepository.findByAdmCode(regionVO.getAdmCode()).isPresent()) {
                    continue;
                }

                // 가장 최근에 저장되었거나 접근된 bigRegion이 현재 저장하려는 smallRegion을 포함하는 행정 구역이라면
                // DB에 접근하지 않고 캐시된 BigRegion객체를 사용합니다.
                BigRegion bigRegion = regionVO.getBigRegion().equals(latestBigRegion.getBigRegionName()) ? latestBigRegion : bigRegionRepository.findByBigRegionName(latestBigRegion.getBigRegionName()).orElseThrow(() -> new InvalidBigRegionException());
                latestBigRegion = bigRegion;

                SmallRegion newSmallRegion = new SmallRegion(regionVO.getSmallRegion(), regionVO.getAdmCode(), regionVO.getLongitude(), regionVO.getLatitude(), bigRegion);
                smallRegionRepository.save(newSmallRegion);
            }
        }

        log.info("지역 테이블 초기화를 성공적으로 마쳤습니다.");
    }

    private void initializeRegionAdmCodeDict(List<BigRegion> allBigRegionList, List<SmallRegion> allSmallRegionList) {

        bigRegionAdmCodeDict = new Hashtable<>(allBigRegionList.size());
        smallRegionAdmCodeDict = new Hashtable<>(allSmallRegionList.size());

        for (BigRegion bigRegion : allBigRegionList) {
            bigRegionAdmCodeDict.put(bigRegion.getBigRegionName(), bigRegion.getAdmCode());

            smallRegionAdmCodeDict.put(bigRegion.getBigRegionName(), new Hashtable<>());
        }

        for (SmallRegion smallRegion : allSmallRegionList) {
            String bigRegionName = smallRegion.getBigRegion().getBigRegionName();
            String smallRegionName = smallRegion.getSmallRegionName();
            String smallRegionAdmCode = smallRegion.getAdmCode();

            smallRegionAdmCodeDict.get(bigRegionName).put(smallRegionName, smallRegionAdmCode);
        }
    }

    private void initializeRedisRepository(List<BigRegion> allBigRegionList, List<SmallRegion> allSmallRegionList) {
        for (BigRegion bigRegion : allBigRegionList) {
            BigRegionRedisVO bigRegionRedisVO = new BigRegionRedisVO(bigRegion);
            bigRegionRedisRepository.save(bigRegionRedisVO);
        }

        for (SmallRegion smallRegion : allSmallRegionList) {
            SmallRegionRedisVO smallRegionRedisVO = new SmallRegionRedisVO(smallRegion);
            smallRegionRedisRepository.save(smallRegionRedisVO);
        }
    }

    private boolean checkRegionTableInitialized(List<RegionCsvVO> regionList) {

        // 레포지토리가 비어있다면 초기화가 되어있지 않다고 간주합니다.
        if (bigRegionRepository.count() == 0 && smallRegionRepository.count() == 0) {
            return false;
        }

        for (RegionCsvVO regionVO : regionList) {

            // DB에 존재하는지 확인합니다.
            boolean isAlreadyExist = regionVO.isBigRegionInfo() ?
                    bigRegionRepository.findByAdmCode(regionVO.getAdmCode()).isPresent() :
                    smallRegionRepository.findByAdmCode(regionVO.getAdmCode()).isPresent();

            if (isAlreadyExist == false) {
                return false;
            }
        }

        return true;
    }

}