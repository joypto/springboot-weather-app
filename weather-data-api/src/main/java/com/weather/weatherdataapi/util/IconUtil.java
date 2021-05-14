package com.weather.weatherdataapi.util;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class IconUtil {

    public static Dictionary<String, String> iconAndDescriptionDict;
    public static Dictionary<String, ArrayList<String>> descriptionAndMessageDict;

    static {
        initializeIconDict();
    }

    private static void initializeIconDict() {
        iconAndDescriptionDict = new Hashtable<>();
        descriptionAndMessageDict = new Hashtable<>();
    }

}
