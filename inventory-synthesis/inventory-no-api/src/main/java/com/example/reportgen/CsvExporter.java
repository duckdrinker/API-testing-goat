package com.example.reportgen;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CsvExporter {

    public static String toCsv(List<SalesRecord> records) {
        String header = "product,amount,region";
        String rows = records.stream()
                .map(r -> String.join(",",
                        StringEscapeUtils.escapeCsv(r.getProduct()),
                        String.valueOf(r.getAmount()),
                        StringEscapeUtils.escapeCsv(r.getRegion())))
                .collect(Collectors.joining("\n"));
        return header + "\n" + rows + "\n";
    }
}
