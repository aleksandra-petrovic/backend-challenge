package com.nordeus.jobfair.auctionservice.auctionservice.domain;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionNotifer;
import com.nordeus.jobfair.auctionservice.auctionservice.repositories.AuctionRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repositories.BidRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repositories.PlayerRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repositories.UserRepository;
import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@AllArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionNotifer auctionNotifier;
    private final AuctionRepository auctionRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    @Override
    public Collection<Auction> getAllActive() {
        return auctionRepository.findAuctionsByActiveTrue();
    }

    @Override
    public Auction getAuction(Long auctionId) {
        return auctionRepository.findByAuctionId(auctionId);
    }

    @Override
    public void join(Long auctionId, Long userId) {
        User user = userRepository.findUserByUserId(userId);
        user.setAuction(auctionRepository.findByAuctionId(auctionId));
        userRepository.save(user);
    }

    @Override
    public void exit(Long userId){
        User user = userRepository.findUserByUserId(userId);
        user.setAuction(null);
        userRepository.save(user);
    }

    @Override
    public String bid(Long auctionId, Long userId) {

        Auction auction = auctionRepository.findByAuctionId(auctionId);
        User user = userRepository.findUserByUserId(userId);

        /*
        Checking if given user has joined the auction before bidding on it.
         */
        if(auction != null && user != null && auction.getJoinedUsers().contains(user)){
            Bid newBid = new Bid(); // the bid that user is currently placing
            List<Bid> existingBids = auction.getBids();
            int lastBid = !existingBids.isEmpty() ?
                    existingBids.get(existingBids.size() - 1).getTokens() : 0; // highest bid at the moment of placing

            /*
            Checking if given user has enough tokens to match the bid.
             */
            if(user.getTokens() > lastBid){
                newBid.setUser(user);
                newBid.setAuction(auction);
                newBid.setTokens(lastBid + 1);
                newBid.setTime(new Date());

                user.setTokens(user.getTokens() - 1);

                bidRepository.save(newBid);
                auctionNotifier.bidPlaced(newBid);
                return "Successfully bid on an auction for player: " + auction.getPlayer();
            } else {
                return "You don't have enough tokens to bid on this auction. Try another one.";
            }
        } else {
            return "Can't place a bid on an auction you didn't join.";
        }
    }

    /**
     * Scheduled method that handles generating new auctions every minute.
     * First call of this method is in the BootstrapData class and that is the reason
     * for the initial delay.
     */
    @Override
    @Scheduled(initialDelay = 60000, fixedRate = 60000)
    @Transactional
    public void generateAuctions() {

        Random random = new Random();
        int counter = 0;
        int timeLimit = 60;

        /*
        This loop will fill the database with 10 fresh auctions, while ensuring there
        are no duplicates.
         */
        while(counter < 10){
            long playerId = random.nextInt(playerRepository.findAll().size()) + 1;
            Player player = playerRepository.findByPlayerId(playerId);

            if(auctionRepository.findAuctionByPlayerEquals(player) == null){
                Auction auction = new Auction();
                auction.setActive(true);
                auction.setTimeLimit(timeLimit);
                auction.setPlayer(player);
                auctionRepository.save(auction);
                counter++;
            }
        }

        auctionNotifier.activeAuctionsRefreshed(auctionRepository.findAuctionsByActiveTrue());
    }

    /**
     * Scheduled method for counting down seconds until the end of auctions.
     */
    @Override
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void countdown() {
        long currentTime = System.currentTimeMillis();
        int prolongedSeconds = 5;
        int lastChanceSeconds = 5;

        auctionRepository.findAuctionsByActiveTrue().forEach(auction -> {
            if (auction.getTimeLimit() > 0) {
                auction.setTimeLimit(auction.getTimeLimit() - 5);

                if (auction.getTimeLimit() == 0 && auction.getBids().stream().anyMatch(bid ->
                        bid.getTime().after(new Date(currentTime - (lastChanceSeconds * 1000))))) {
                    // if any bid happened in the last 5 seconds
                    auction.setTimeLimit(prolongedSeconds); // prolong the auction for 5 more seconds
                } else if (auction.getTimeLimit() == 0) {
                    // if there were no bids in last 5 seconds - finish the auction
                    auctionNotifier.auctionFinished(auction);
                    auctionNotifier.activeAuctionsRefreshed(auctionRepository.findAuctionsByActiveTrue());
                    auctionRepository.delete(auction); // no need to store it in the database anymore - free up space
                }
            }
        });

    }
}