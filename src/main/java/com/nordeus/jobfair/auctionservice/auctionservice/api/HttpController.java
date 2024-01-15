package com.nordeus.jobfair.auctionservice.auctionservice.api;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor
@RestController
@CrossOrigin
@RequestMapping(path = "/auctions")
public class HttpController {

    private AuctionService auctionService;

    /**
     * Here and in the methods down below I am simulating authorization that would normally go through
     * JWT and Spring Security. Instead of JWT, a String value is passed that holds user id. That way I
     * can identify from whom the request is coming from.
     * @param userId String
     * @return list of active auctions
     */

    @GetMapping(value = "/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Auction> getAllActive(@RequestHeader(HttpHeaders.AUTHORIZATION) String userId) {
        /*
        Because a request for a list of active auctions has been received, it means that the user
        navigated to the corresponding page, which further means he exited any auction he previously
        joined, which I am ensuring right here below.
         */
        auctionService.exit(Long.valueOf(extractUserId(userId)));
        return auctionService.getAllActive();
    }

    @GetMapping(value = "/{auctionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAuctionById(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionService.getAuction(auctionId);
        if(auction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Auction was not found in the database. It has probably expired. Better luck next time. :)");
        } else {
            return ResponseEntity.ok().body(auction);
        }
    }

    @GetMapping(value = "/{auctionId}/join", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> joinAuction(@PathVariable("auctionId") Long auctionId, @RequestHeader(HttpHeaders.AUTHORIZATION) String userId){
        Auction auction = auctionService.getAuction(auctionId);
        if(auction == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are trying to join an expired auction. Better luck next time. :)");
        } else {
            auctionService.join(auctionId, Long.valueOf(extractUserId(userId)));
            return ResponseEntity.ok("Successfully joined an auction for player: " + auction.getPlayer());
        }
    }

    @GetMapping(value = "/{auctionId}/bid", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> bidOnAuction(@PathVariable("auctionId") Long auctionId, @RequestHeader(HttpHeaders.AUTHORIZATION) String userId){
        Auction auction = auctionService.getAuction(auctionId);

        if(auction == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You are trying to bid on an expired auction. Better luck next time. :)");
        } else {
            return ResponseEntity.ok().body(auctionService.bid(auctionId, Long.valueOf(extractUserId(userId))));
        }
    }

    public String extractUserId(String token){
        if(token != null && token.startsWith("Bearer ")) return token.substring(7);
        else return "";
    }

}
