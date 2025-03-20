package com.tus.service.service;

import com.tus.service.model.FlightBooking;
import com.tus.service.repository.FlightBookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FlightService {

        @Autowired
        FlightBookingRepo flightBookingRepo;


    public Optional<FlightBooking> getFlightByNumber(String flightNumber) {
        return flightBookingRepo.findByFlightNumber(flightNumber);
    }

    public FlightBooking save(FlightBooking flight) {

        return flightBookingRepo.save(flight);
    }

    public void delete(FlightBooking flight) {
        flightBookingRepo.delete(flight);
    }

    public Object findAll() {
        return flightBookingRepo.findAll();
    }
}

