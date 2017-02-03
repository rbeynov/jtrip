package com.ynov.jtrip.dao;

import com.ynov.jtrip.dao.jdbc.JdbcPlaceDao;
import com.ynov.jtrip.dao.jdbc.JdbcTripDao;
import com.ynov.jtrip.util.ConnectionManager;

public class DaoFactory {


    private static JdbcPlaceDao jdbcPlaceDao;
    private static JdbcTripDao jdbcTripDao;

    private DaoFactory() {

    }

    public static PlaceDao getPlaceDao() {
        if (jdbcPlaceDao == null) {
            jdbcPlaceDao = new JdbcPlaceDao(ConnectionManager.getConnection());
        }

        return jdbcPlaceDao;
    }

    public static TripDao getTripDao() {
        if(jdbcTripDao == null) {
            jdbcTripDao = new JdbcTripDao(ConnectionManager.getConnection());
        }

        return jdbcTripDao;
    }
}
