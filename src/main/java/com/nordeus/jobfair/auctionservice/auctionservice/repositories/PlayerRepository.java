package com.nordeus.jobfair.auctionservice.auctionservice.repositories;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByPlayerId(Long playerId);
}
