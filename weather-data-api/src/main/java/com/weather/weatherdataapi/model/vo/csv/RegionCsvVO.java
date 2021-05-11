package com.weather.weatherdataapi.model.vo.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Getter;

@Getter
public class RegionCsvVO {

    @JsonProperty("adm_code")
    private String admCode;

    @JsonProperty("big_region")
    private String bigRegion;

    @JsonProperty("small_region")
    private String smallRegion;

    @JsonProperty("longitude")
    private String longitude;

    @JsonProperty("latitude")
    private String latitude;

    public static CsvSchema getSchema() {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("adm_code", CsvSchema.ColumnType.STRING)
                .addColumn("big_region", CsvSchema.ColumnType.STRING)
                .addColumn("small_region", CsvSchema.ColumnType.STRING)
                .addColumn("longitude", CsvSchema.ColumnType.STRING)
                .addColumn("latitude", CsvSchema.ColumnType.STRING)
                .build().withHeader();

        return schema;
    }

    public boolean isBigRegionInfo() {
        return smallRegion.trim().length() == 0;
    }

    @Override
    public String toString() {
        return "RegionCsvVO{" +
                "admCode='" + admCode + '\'' +
                ", bigRegion='" + bigRegion + '\'' +
                ", smallRegion='" + smallRegion + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }

}
