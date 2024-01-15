package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class AuctionNotifierLogger implements AuctionNotifer {
    @Override
    public void auctionFinished(Auction auction) {
        if(!auction.getBids().isEmpty()){
            log.info("Auction finished: {}, winner was {}", auction, auction.getBids().get(auction.getBids().size() - 1).getUser());
        }
        /*
        Else - no need to notify anyone, because no bids have been made.
         */
    }
    @Override
    public void bidPlaced(Bid bid) {
        log.info("Bid placed: {}", bid);
    }

    @Override
    public void activeAuctionsRefreshed(Collection<Auction> activeAuctions) {
        log.info("Active auctions are refreshed: {}", activeAuctions);
    }

}
