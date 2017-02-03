package com.ynov.jtrip.dao.jdbc;

import com.ynov.jtrip.dao.DataAccessException;
import com.ynov.jtrip.dao.TripDao;
import com.ynov.jtrip.model.Place;
import com.ynov.jtrip.model.Trip;

import java.sql.*;

public class JdbcTripDao extends JdbcDao implements TripDao {


    public JdbcTripDao(Connection connection) {
        super(connection);
    }

    @Override
    public boolean removeTrip(Trip trip) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM trips WHERE id = ?");
            statement.setLong(1, trip.getId());

            // Execute update
            int deletedRows = statement.executeUpdate();

            // Commit the update
            getConnection().commit();

            return deletedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to delete this trip: " + trip, e);
        }
    }

    @Override
    public Long createTrip(Trip trip) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO trips(departure, destination, price) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, trip.getDeparture().getId());
            statement.setLong(2, trip.getDestination().getId());
            statement.setLong(3, trip.getPrice());

            // Execute insert
            statement.executeUpdate();

            // Get the auto-generated id
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            Long id = resultSet.getLong(1);

            // Commit the update
            getConnection().commit();

            return id;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to insert this trip: " + trip, e);
        }
    }

    @Override
    public Trip findTripById(Long id) {
        Trip foundTrip = null;

        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT " +
                    "trip.id, departure.id, departure.name, destination.id, destination.name, trip.price " +
                    "FROM trips trip " +
                    "JOIN places departure ON departure.id = trip.departure " +
                    "JOIN places destination ON destination.id = trip.destination " +
                    "WHERE trip.id = ?");
            statement.setLong(1, id);

            // Execute find
            ResultSet resultSet = statement.executeQuery();

            // Map results
            if (resultSet.next()) {
                foundTrip = new Trip();
                foundTrip.setId(resultSet.getLong(1));

                Place departure = new Place();
                departure.setId(resultSet.getLong(2));
                departure.setName(resultSet.getString(3));
                foundTrip.setDeparture(departure);

                Place destination = new Place();
                destination.setId(resultSet.getLong(4));
                destination.setName(resultSet.getString(5));
                foundTrip.setDestination(destination);

                foundTrip.setPrice(resultSet.getLong(6));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to find trip with id: " + id, e);
        }

        return foundTrip;
    }
}
