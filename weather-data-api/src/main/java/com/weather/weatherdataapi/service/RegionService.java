package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.entity.BigRegion;
import com.weather.weatherdataapi.model.entity.SmallRegion;
import com.weather.weatherdataapi.model.vo.csv.RegionCsvVO;
import com.weather.weatherdataapi.repository.BigRegionRepository;
import com.weather.weatherdataapi.repository.SmallRegionRepository;
import com.weather.weatherdataapi.util.CsvParserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegionService {

    private final BigRegionRepository bigRegionRepository;
    private final SmallRegionRepository smallRegionRepository;

    public void initialize() {
        try {

            log.info("initialize::resources/data/region.csv 파일을 읽어 지역정보를 초기화합니다.");

            ClassPathResource regionCsvResource = new ClassPathResource("data/region.csv");
            List<RegionCsvVO> regionList = CsvParserUtil.parseCsvToObject(RegionCsvVO.class, regionCsvResource.getFile(), RegionCsvVO.getSchema());

            if (checkRegionTableInitialized(regionList) == true) {
                log.info("initialize::모든 지역정보가 이미 DB에 존재합니다.");
            } else {
                initializeRegionTable(regionList);
                log.info("initialize::초기화를 성공적으로 마쳤습니다.");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            log.error("initialize::초기화하는 도중 문제가 발생하였습니다.");
        }
    }

    @Transactional
    public void initializeRegionTable(List<RegionCsvVO> regionList) {
        bigRegionRepository.deleteAll();
        smallRegionRepository.deleteAll();

        BigRegion latestBigRegion = null;

        for (RegionCsvVO regionVO : regionList) {
            // BigRegion 에 대한 정보입니다.
            // BigRegion 정보에 대한 행은 smallRegion 필드의 값이 비어있는 행입니다.
            if (regionVO.isBigRegionInfo() == true) {

                // 이미 존재한다면 추가하지 않습니다.
                if (bigRegionRepository.findByAdmCode(regionVO.getAdmCode()) != null) {
                    continue;
                }

                BigRegion newBigRegion = new BigRegion(regionVO.getBigRegion(), regionVO.getAdmCode(), regionVO.getLongitude(), regionVO.getLatitude());
                bigRegionRepository.save(newBigRegion);

                latestBigRegion = newBigRegion;
            }
            // 그 외에는 전부 소분류 지역에 대한 정보입니다.
            else {

                // 이미 존재한다면 추가하지 않습니다.
                if (smallRegionRepository.findByAdmCode(regionVO.getAdmCode()) != null) {
                    continue;
                }

                // 가장 최근에 저장되었거나 접근된 bigRegion이 현재 저장하려는 smallRegion을 포함하는 행정 구역이라면
                // DB에 접근하지 않고 캐시된 BigRegion객체를 사용합니다.
                BigRegion bigRegion = regionVO.getBigRegion().equals(latestBigRegion.getBigRegionName()) ? latestBigRegion : bigRegionRepository.findByBigRegionName(latestBigRegion.getBigRegionName());
                latestBigRegion = bigRegion;

                SmallRegion newSmallRegion = new SmallRegion(regionVO.getSmallRegion(), regionVO.getAdmCode(), regionVO.getLongitude(), regionVO.getLatitude(), bigRegion);
                smallRegionRepository.save(newSmallRegion);
            }
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
                    bigRegionRepository.findByAdmCode(regionVO.getAdmCode()) != null :
                    smallRegionRepository.findByAdmCode(regionVO.getAdmCode()) != null;

            if (isAlreadyExist == false) {
                return false;
            }
        }

        return true;
    }

}