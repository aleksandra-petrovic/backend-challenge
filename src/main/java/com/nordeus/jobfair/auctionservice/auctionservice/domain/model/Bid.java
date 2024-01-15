package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bidId;

    @Column(nullable = false)
    private Integer tokens;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date time;

    @ManyToOne
    @JoinColumn(name = "bidder", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "auction", referencedColumnName = "auctionId")
    private Auction auction;

    @Override
    public String toString() {
        return "[Bid: " + user + " ; " + auction + " ; " + tokens + "]";
    }
}
