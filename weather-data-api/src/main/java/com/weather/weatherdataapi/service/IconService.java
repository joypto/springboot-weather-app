package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.vo.csv.IconCsvVO;
import com.weather.weatherdataapi.model.vo.csv.MessageCsvVO;
import com.weather.weatherdataapi.util.CsvParserUtil;
import com.weather.weatherdataapi.util.ExceptionUtil;
import com.weather.weatherdataapi.util.IconUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IconService {

    public String getRandomIconMessage(String icon) {
        String description = IconUtil.iconAndDescriptionDict.get(icon);
        ArrayList<String> messageList = IconUtil.descriptionAndMessageDict.get(description);
        Collections.shuffle(messageList);
        return messageList.get(0);
    }

    public void initialize() {
        try {
            log.info("resources/data/icon.csv 파일을 읽어 아이콘 정보를 초기화합니다.");
            long startTime = System.currentTimeMillis();

            ClassPathResource iconCsvResource = new ClassPathResource("/data/icon.csv");
            ClassPathResource messageCsvResource = new ClassPathResource("/data/message.csv");

            List<IconCsvVO> iconList = CsvParserUtil.parseCsvToObject(IconCsvVO.class, iconCsvResource.getInputStream(), IconCsvVO.getSchema());
            List<MessageCsvVO> messageList = CsvParserUtil.parseCsvToObject(MessageCsvVO.class, messageCsvResource.getInputStream(), MessageCsvVO.getSchema());

            initializedIconDict(iconList, messageList);

            long endTime = System.currentTimeMillis();
            float diffTimeSec = (endTime - startTime) / 1000f;
            log.info("초기화를 성공적으로 마쳤습니다. ({}sec)", diffTimeSec);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(ExceptionUtil.getStackTraceString(e));
            log.error("초기화하는 도중 문제가 발생하였습니다.");
        }
    }

    public void initializedIconDict(List<IconCsvVO> iconList, List<MessageCsvVO> messageList) {

        for (IconCsvVO iconVO : iconList) {
            IconUtil.iconAndDescriptionDict.put(iconVO.getDayIcon(),iconVO.getDescription());
            IconUtil.iconAndDescriptionDict.put(iconVO.getNightIcon(),iconVO.getDescription());
        }

        for (MessageCsvVO messageVO : messageList) {
            if (IconUtil.descriptionAndMessageDict.get(messageVO.getDescription()) != null) {
                ArrayList<String> valueList = IconUtil.descriptionAndMessageDict.get(messageVO.getDescription());
                valueList.add(messageVO.getMessage());
            } else {
                ArrayList<String> valueList = new ArrayList<>();
                IconUtil.descriptionAndMessageDict.put(messageVO.getDescription(), valueList);
            }
        }

        log.info("아이콘 딕셔너리 초기화를 성공적으로 마쳤습니다.");
    }

}
