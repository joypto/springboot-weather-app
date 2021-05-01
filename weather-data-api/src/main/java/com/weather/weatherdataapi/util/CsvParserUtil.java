package com.weather.weatherdataapi.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvParserUtil {

    public static <T> List<T> parseCsvToObject(Class<T> responseClass, File file, CsvSchema schema) throws IOException {
        List<T> results = new ArrayList<>();

        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<T> mappingIterator = csvMapper.readerFor(responseClass).with(schema).readValues(file);

        results = mappingIterator.readAll();
        return results;
    }

    public static <T> List<T> parseCsvToObject(Class<T> responseClass, InputStream inputStream, CsvSchema schema) throws IOException {
        List<T> results = new ArrayList<>();

        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<T> mappingIterator = csvMapper.readerFor(responseClass).with(schema).readValues(inputStream);

        results = mappingIterator.readAll();
        return results;
    }

}
