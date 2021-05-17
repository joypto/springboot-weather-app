package com.weather.weatherdataapi.model.vo.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Getter;

@Getter
public class IconCsvVO {

    @JsonProperty("day_icon")
    private String dayIcon;

    @JsonProperty("night_icon")
    private String nightIcon;

    @JsonProperty("description")
    private String description;

    public static CsvSchema getSchema() {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("day_icon", CsvSchema.ColumnType.STRING)
                .addColumn("night_icon", CsvSchema.ColumnType.STRING)
                .addColumn("description", CsvSchema.ColumnType.STRING)
                .build().withHeader();

        return schema;
    }

}
