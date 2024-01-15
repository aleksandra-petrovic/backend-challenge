package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

    @Column(nullable = false)
    private String playerFullName;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Double quality;

    @Column(nullable = false)
    private String value;

    @Override
    public String toString() {
        return "[Player: " + playerFullName + "]";
    }
}
