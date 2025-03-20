package com.tus.service.repository;

import com.tus.service.model.FlightBooking;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FlightBookingRepo extends CrudRepository<FlightBooking,Long> {

    Optional<FlightBooking> findByFlightNumber(String flightNumber);
}
