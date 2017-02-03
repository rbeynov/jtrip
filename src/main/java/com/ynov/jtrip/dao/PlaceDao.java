package com.ynov.jtrip.dao;


import com.ynov.jtrip.model.Place;

import java.util.List;

public interface PlaceDao {

    Long createPlace(Place place);

    Place findPlaceById(Long id);

    boolean updatePlace(Place place);

    boolean removePlace(Place place);

    List<Place> findAllPlaces();
}
