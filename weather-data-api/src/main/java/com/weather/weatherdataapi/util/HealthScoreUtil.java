package com.weather.weatherdataapi.util;

import java.util.Dictionary;
import java.util.Hashtable;

public class HealthScoreUtil {

    private static Dictionary<String, Integer> healthScoreDict;

    static {
        initializeHealthScoreDict();
    }

    private static void initializeHealthScoreDict() {
        healthScoreDict = new Hashtable<>();

        // 보건 기상지수의 값을 백점 만점의 점수로 변환해주는 해시테이블입니다.
        healthScoreDict.put("0", 100);
        healthScoreDict.put("1", 70);
        healthScoreDict.put("2", 40);
        healthScoreDict.put("3", 10);

    }

    public static Integer convertHealthWthIdxToScore(String idx) {
        return healthScoreDict.get(idx);
    }

}
