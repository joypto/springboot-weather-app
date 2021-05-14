package com.weather.weatherdataapi.model.vo.csv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.Getter;

@Getter
public class MessageCsvVO {

    @JsonProperty("description")
    private String description;

    @JsonProperty("message")
    private String message;

    private static CsvSchema getSchema() {
        CsvSchema schema = CsvSchema.builder()
                .addColumn("description", CsvSchema.ColumnType.STRING)
                .addColumn("message", CsvSchema.ColumnType.STRING)
                .build().withHeader();

        return schema;
    }

}
