package com.weather.weatherdataapi;

import java.util.Dictionary;
import java.util.List;

public final class Global {


    public final static String IDENTIFICATION_TEXT = "Identification";
    public final static String USER_AGENT_TEXT = "user-agent";

    public enum EInfoResponseCode {
        NORMAL("일반적인 응답입니다."),
        ERROR_FETCH_FAILED("원격 서버에서 정보를 가져오는 데 실패하였습니다.");

        private final String message;

        EInfoResponseCode(String name) {
            this.message = name;
        }

        public String getMessage() {
            return message;
        }

    }

}
