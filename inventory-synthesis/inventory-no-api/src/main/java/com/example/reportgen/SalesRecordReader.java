package com.example.reportgen;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Path;
import java.util.List;

public class SalesRecordReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<SalesRecord> readFrom(Path jsonFile) throws Exception {
        return MAPPER.readValue(jsonFile.toFile(),
                MAPPER.getTypeFactory().constructCollectionType(List.class, SalesRecord.class));
    }
}
