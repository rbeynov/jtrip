package com.ynov.jtrip.dao.jdbc;

import com.ynov.jtrip.dao.DataAccessException;
import com.ynov.jtrip.dao.PlaceDao;
import com.ynov.jtrip.model.Place;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPlaceDao extends JdbcDao implements PlaceDao {

    public JdbcPlaceDao(Connection connection) {
        super(connection);
    }

    @Override
    public Place findPlaceById(Long id) {
        Place foundPlace = null;

        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT id, name FROM places WHERE id = ?");
            statement.setLong(1, id);

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Get first result
            if (resultSet.next()) {
                foundPlace = toPlace(resultSet);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to retrieve place with id: " + id, e);
        }

        return foundPlace;
    }
    
    @Override
    public Long createPlace(Place place) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO places(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, place.getName());

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
            throw new DataAccessException("Unable to insert this place: " + place, e);
        }
    }

    @Override
    public boolean updatePlace(Place place) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE places SET name = ? WHERE id = ?");
            statement.setString(1, place.getName());
            statement.setLong(2, place.getId());

            // Execute update
            int editedRows = statement.executeUpdate();

            // Commit the update
            getConnection().commit();

            return editedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to update this place: " + place, e);
        }
    }

    @Override
    public boolean removePlace(Place place) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM places WHERE id = ?");
            statement.setLong(1, place.getId());

            // Execute update
            int editedRows = statement.executeUpdate();

            // Commit the update
            getConnection().commit();

            return editedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Unable to delete this place: " + place, e);
        }
    }

    @Override
    public List<Place> findAllPlaces() {
        List<Place> foundPlaces = new ArrayList<>();

        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT id, name FROM places");

            // Execute query
            ResultSet resultSet = statement.executeQuery();

            // Add all results
            while (resultSet.next()) {
                Place aPlace = toPlace(resultSet);
                foundPlaces.add(aPlace);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to find all places");
        }

        return foundPlaces;
    }

    private Place toPlace(ResultSet resultSet) throws SQLException {
        Place place = new Place();

        place.setId(resultSet.getLong(1));
        place.setName(resultSet.getString(2));

        return place;
    }
}
