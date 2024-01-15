package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import org.apache.commons.csv.CSVRecord;

import java.util.List;

public interface PlayerService {

    List<CSVRecord> getPlayersFromCSV();

    void loadPlayersToDatabase(List<CSVRecord> csvRecords);

    /**
     * helper method
     * takes a value that is supposed to be scaled and two intervals (old and new)
     * applies general formula for linear scaling
     */
    static double scaleValue(double value, double min1, double max1, double min2, double max2){
        return ((value - min1) * (max2 - min2) / (max1 - min1)) + min2;
    }
}
