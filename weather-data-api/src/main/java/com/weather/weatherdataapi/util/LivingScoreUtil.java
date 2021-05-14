package com.weather.weatherdataapi.util;

import java.util.Dictionary;
import java.util.Hashtable;

public class LivingScoreUtil {

    private static Dictionary<String, Integer> livingScoreDict;

    static {
        initializeHealthScoreDict();
    }

    private static void initializeHealthScoreDict() {
        livingScoreDict = new Hashtable<>();

        // 보건 기상지수의 값을 백점 만점의 점수로 변환해주는 해시테이블입니다.
        livingScoreDict.put("0", 100);
        livingScoreDict.put("1", 100);
        livingScoreDict.put("2", 100);
        livingScoreDict.put("3", 70);
        livingScoreDict.put("4", 70);
        livingScoreDict.put("5", 70);
        livingScoreDict.put("6", 40);
        livingScoreDict.put("7", 40);
        livingScoreDict.put("8", 10);
        livingScoreDict.put("9", 10);
        livingScoreDict.put("10", 10);
        livingScoreDict.put("11", 10);
        livingScoreDict.put("12", 10);

    }

    public static Integer convertHealthWthIdxToScore(String idx) {
        return livingScoreDict.get(idx);
    }

    // 생활기상지수 중 자외선 지수의 점수 변환
    public static Integer convertUvInfoToScore(Integer wthIdx) {

        if (wthIdx <= 2)
            return 100;
        else if (wthIdx <= 5)
            return 70;
        else if (wthIdx <= 7)
            return 40;
        else if (wthIdx <= 12)
            return 10;
        else
            return 0;

    }
}
