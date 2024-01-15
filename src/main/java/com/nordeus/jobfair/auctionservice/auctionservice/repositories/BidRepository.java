package com.nordeus.jobfair.auctionservice.auctionservice.repositories;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    Bid findByBidId(Long bidId);
}
