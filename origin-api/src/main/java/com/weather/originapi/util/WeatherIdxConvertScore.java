package com.weather.originapi.util;

import java.util.Dictionary;
import java.util.Hashtable;

public class WeatherIdxConvertScore {

    private static Dictionary<String, Integer> healthScoreDict;

    static {
        InitializeHealthScoreDict();
    }

    private static void InitializeHealthScoreDict() {
        healthScoreDict = new Hashtable<>();

        healthScoreDict.put("0", 100);
        healthScoreDict.put("1", 70);
        healthScoreDict.put("2", 40);
        healthScoreDict.put("3", 10);

    }

    public static Integer convertHealthWthIdxToScore(String idx) {
        return healthScoreDict.get(idx);
    }


}
