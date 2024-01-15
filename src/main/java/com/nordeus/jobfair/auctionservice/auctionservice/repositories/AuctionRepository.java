package com.nordeus.jobfair.auctionservice.auctionservice.repositories;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    Auction findByAuctionId(Long auctionId);
    Auction findAuctionByPlayerEquals(Player player);
    List<Auction> findAuctionsByActiveTrue();
}
