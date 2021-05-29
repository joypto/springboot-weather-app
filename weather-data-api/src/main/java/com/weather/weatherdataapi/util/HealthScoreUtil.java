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

    public static Integer convertHealthWthIdxToScore(Integer idx) {
        return healthScoreDict.get(idx);
    }

    // 보건기상지수 중 식중독 지수 제외한 그 외 지수의 점수 변환
    public static Integer convertHealthInfoToScore(Integer wthIdx) {

        switch (wthIdx) {
            case 0:
                return 100;
            case 1:
                return 70;
            case 2:
                return 40;
            case 3:
                return 10;
            default:
                return 0;
        }

    }

    // 보건기상지수 중 식중독 지수의 점수 반환
    public static Integer convertFoodPoisonInfoToScore(Integer wthIdx) {
        if (wthIdx <= 55) {
            return 100;
        } else if (wthIdx <= 70) {
            return 70;
        } else if (wthIdx <= 85) {
            return 40;
        }
        return 10;
    }

}
