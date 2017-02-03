package com.ynov.jtrip.dao;

import com.ynov.jtrip.model.Trip;

public interface TripDao {

    Long createTrip(Trip trip);

    Trip findTripById(Long id);

    boolean removeTrip(Trip trip);
}
