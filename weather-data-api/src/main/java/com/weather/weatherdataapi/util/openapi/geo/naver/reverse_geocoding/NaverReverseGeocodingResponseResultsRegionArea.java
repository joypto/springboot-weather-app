package com.weather.weatherdataapi.util.openapi.geo.naver.reverse_geocoding;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class NaverReverseGeocodingResponseResultsRegionArea {

    @SerializedName("name")
    private String name;

    @SerializedName("coords")
    private NaverReverseGeocodingResponseResultsRegionAreaCoords coords;

    @SerializedName("alias")
    private String alias;

    @Getter
    public class NaverReverseGeocodingResponseResultsRegionAreaCoords {

        @SerializedName("center")
        private NaverReverseGeocodingResponseResultsRegionAreaCoordsCenter center;

        @Getter
        public class NaverReverseGeocodingResponseResultsRegionAreaCoordsCenter {

            @SerializedName("crs")
            private String crs;

            @SerializedName("x")
            private Double x;

            @SerializedName("y")
            private Double y;
        }
    }
}
