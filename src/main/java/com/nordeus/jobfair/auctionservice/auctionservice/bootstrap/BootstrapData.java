package com.nordeus.jobfair.auctionservice.auctionservice.bootstrap;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionServiceImpl;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.PlayerService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.PlayerServiceImpl;
import com.nordeus.jobfair.auctionservice.auctionservice.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Purpose of this component is to initialize database
 * Because it implements CommandLineRunner, the code within this class
 * will be executed at application startup
 */

@Component
@AllArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PlayerService playerService;
    private final AuctionService auctionService;

    @Override
    public void run(String... args) {

        List<CSVRecord> csvRecords = playerService.getPlayersFromCSV();
        playerService.loadPlayersToDatabase(csvRecords);

        String username = "user";

        for(int i = 1; i <= 20 ; i++){
            User user = new User();
            user.setUsername(username + i);
            user.setTokens(50);
            userRepository.save(user);
        }

        auctionService.generateAuctions();

        System.out.println("Data loaded!");
    }
}
