package com.nordeus.jobfair.auctionservice.auctionservice.repositories;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserId(Long userId);
}
