package com.nordeus.jobfair.auctionservice.auctionservice.domain;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;

import java.util.Collection;

public interface AuctionService {
    Collection<Auction> getAllActive();
    Auction getAuction(Long auctionId);
    void join(Long auctionId, Long userId);
    String bid(Long auctionId, Long userId);
    void generateAuctions();
    void countdown();
    void exit(Long userId);
}
