package com.tus.service.controller;
import com.tus.service.model.FlightBooking;
import com.tus.service.service.FlightService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
class FlightController {

    @Autowired
    private FlightService service;

    @Value("${jwt.secret}")
    private String secretKey;

    @GetMapping
    public ResponseEntity<?> getAllFlights(@RequestHeader("Authorization") String token) {
        try {
            if (!validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
            }
            return ResponseEntity.ok(service.findAll());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getFlightByNumber(
            @RequestHeader("Authorization") String token,
            @RequestParam String flightNumber) {

        if (!validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        return service.getFlightByNumber(flightNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @PostMapping
    public ResponseEntity<?> createFlight(@RequestHeader("Authorization") String token, @RequestBody FlightBooking flight) {
        try {
            if (!validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
            }
            //flight.setId(UUID.randomUUID().toString());
            flight = service.save(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(flight);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PutMapping
    public ResponseEntity<?> updateFlight(@RequestHeader("Authorization") String token, @RequestBody FlightBooking flight) {
        try {
            if (!validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
            }
            //flight.setId(UUID.randomUUID().toString());
            service.save(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(flight);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteFlight(@RequestHeader("Authorization") String token, @RequestBody FlightBooking flight) {
        try {
            if (!validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
            }
            //flight.setId(UUID.randomUUID().toString());
            service.delete(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(flight);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    private boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new RuntimeException("Token has expired.");
        } catch (io.jsonwebtoken.SignatureException e) {
            throw new RuntimeException("Invalid token signature.");
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new RuntimeException("Malformed token.");
        } catch (Exception e) {
            throw new RuntimeException("Invalid token.");
        }
    }

}

