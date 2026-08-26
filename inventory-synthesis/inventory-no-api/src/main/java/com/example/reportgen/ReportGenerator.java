package com.example.reportgen;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReportGenerator {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: ReportGenerator <input.json> <output.csv>");
            System.exit(1);
        }
        Path input = Path.of(args[0]);
        Path output = Path.of(args[1]);

        List<SalesRecord> records = SalesRecordReader.readFrom(input);
        String csv = CsvExporter.toCsv(records);
        Files.writeString(output, csv);

        System.out.println("Wrote " + records.size() + " records to " + output);
    }
}
