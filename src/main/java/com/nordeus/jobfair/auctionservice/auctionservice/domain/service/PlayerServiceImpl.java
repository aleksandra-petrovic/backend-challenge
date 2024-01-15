package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.helper.CSVReader;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;
import com.nordeus.jobfair.auctionservice.auctionservice.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService{

    private final PlayerRepository playerRepository;
    private final ResourceLoader resourceLoader;

    /**
     * Loads data from a resource found on classpath of the project.
     * Retrieves a list of rows from found data source.
     */
    @Override
    public List<CSVRecord> getPlayersFromCSV(){
        try {
            Resource resource = resourceLoader.getResource("classpath:EuropeanPlayersInfo.csv");
            String filePath = resource.getFile().getAbsolutePath();
            return CSVReader.readCSV(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Iterates through a list of rows.
     * Parses each row to match fields of "Player" entity.
     * Uses injected repository instance to save players to local database.
     *
     * @param csvRecords list of rows from a data source
     */
    @Override
    public void loadPlayersToDatabase(List<CSVRecord> csvRecords){
        for(CSVRecord record : csvRecords) {

            String[] values = record.values()[0].split(";");

            Player player = new Player();

            player.setPlayerFullName(values[0]);
            player.setAge(Integer.valueOf(values[1]));
            player.setRole(values[2]);
            player.setValue(values[3]);

            /*
              Scales a ranking represented in the data source from a 1-100 interval
              to a 1-5 interval, so it could match Top Eleven standard for measuring player
              quality (* - *****).
             */
            double qlty = PlayerService.scaleValue(Double.parseDouble(values[4]), 1.0, 100.0, 1.0, 5.0);
            DecimalFormat df = new DecimalFormat("0.00");
            player.setQuality(Double.valueOf(df.format(qlty)));

            playerRepository.save(player);
        }
    }

}
