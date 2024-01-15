package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Integer tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Bid> bids;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "auction", referencedColumnName = "auctionId")
    private Auction auction;

    @Override
    public String toString() {
        return "[Manager: " + username + "]";
    }
}
