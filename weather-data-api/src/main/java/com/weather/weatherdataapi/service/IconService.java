package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.entity.Icon;
import com.weather.weatherdataapi.model.vo.csv.IconCsvVO;
import com.weather.weatherdataapi.model.vo.csv.MessageCsvVO;
import com.weather.weatherdataapi.repository.IconRepository;
import com.weather.weatherdataapi.util.CsvParserUtil;
import com.weather.weatherdataapi.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IconService {

    private final IconRepository iconRepository;

    public void initialize() {
        try {
            log.info("resources/data/icon.csv 파일을 읽어 아이콘 정보를 초기화합니다.");
            long startTime = System.currentTimeMillis();

            ClassPathResource iconCsvResource = new ClassPathResource("/data/icon.csv");
            ClassPathResource messageCsvResource = new ClassPathResource("/data/message.csv");

            List<IconCsvVO> iconList = CsvParserUtil.parseCsvToObject(IconCsvVO.class, iconCsvResource.getInputStream(), IconCsvVO.getSchema());
            List<MessageCsvVO> messageList = CsvParserUtil.parseCsvToObject(MessageCsvVO.class, messageCsvResource.getInputStream(), MessageCsvVO.getSchema());

            tryInitializedIconTable(iconList, messageList);

            long endTime = System.currentTimeMillis();
            float diffTimeSec = (endTime - startTime) / 1000f;
            log.info("초기화를 성공적으로 마쳤습니다. ({}sec)", diffTimeSec);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("초기화하는 도중 문제가 발생하였습니다.");
        }
    }

    private void tryInitializedIconTable(List<IconCsvVO> iconList, List<MessageCsvVO> messageList) {
        if (checkIconTableInitialized() == true) {
            log.info("모든 아이콘 및 메시지 정보가 이미 DB에 존재합니다.");
            return;
        }

        initializeIconTable(iconList, messageList);
    }

    @Transactional
    public void initializeIconTable(List<IconCsvVO> iconList, List<MessageCsvVO> messageList) {

        for (IconCsvVO iconVO : iconList) {
            Icon newIcon = new Icon(iconVO.getDayIcon(), iconVO.getNightIcon(), iconVO.getDescription());
            iconRepository.save(newIcon);
        }

        for (MessageCsvVO messageVO : messageList) {
            Icon icon = iconRepository.findByDescription(messageVO.getDescription());
            List<String> message = icon.getMessage();
            message.add(messageVO.getMessage());
        }

        log.info("아이콘 테이블 초기화를 성공적으로 마쳤습니다.");
    }

    private boolean checkIconTableInitialized() {

        if(iconRepository.count() == 0) {
            return false;
        }

        return true;
    }

}
