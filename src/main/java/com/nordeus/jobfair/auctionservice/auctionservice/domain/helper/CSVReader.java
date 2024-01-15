package com.nordeus.jobfair.auctionservice.auctionservice.domain.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

/**
 * This class's purpose is only to read CSV files and return records.
 * It is reusable and fits in the modularity of the project.
 */
public class CSVReader {
    public static List<CSVRecord> readCSV(String path) throws Exception{
        Reader reader = new FileReader(path);
        CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);
        return csvParser.getRecords();
    }
}
