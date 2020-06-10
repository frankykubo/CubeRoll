package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.game.cubeRoll.ui.ConsoleUI;
import sk.tuke.gamestudio.entity.Rating;
import java.sql.*;
import java.util.*;

public class RatingServiceJDBC implements RatingService {
    private static Properties properties = ServiceProperties.getServiceProperties();
    private static final String URL = properties.getProperty("jdbc.url");
    private static final String USER = properties.getProperty("jdbc.username");
    private static final String PASSWORD = properties.getProperty("jdbc.password");
    private static final String INSERT_RATING = properties.getProperty("jdbc.rating.INSERT_RATING");
    private static final String SELECT_RATING = properties.getProperty("jdbc.rating.SELECT_RATING");
    private static final String SELECT_ALL_RATINGS = properties.getProperty("jdbc.rating.SELECT_ALL_RATINGS");
    private static final String SELECT_AVG_RATING = properties.getProperty("jdbc.rating.SELECT_AVG_RATING");
    private final static String DELETE_RATING = properties.getProperty("jdbc.rating.DELETE_RATING");

    @Override
    public void setRating(Rating rating) throws RatingException {
        if(rating.getRating() < 1 || rating.getRating() > 5){
            throw new IllegalArgumentException("Rating is below zero or over 5, what is forbidden parameter!");
        }
        boolean added = false;
        while(!added) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                try (PreparedStatement ps = connection.prepareStatement(INSERT_RATING)) {
                    ps.setString(1, rating.getGame());
                    ps.setString(2, rating.getPlayer());
                    ps.setInt(3, rating.getRating());
                    ps.setTimestamp(4, rating.getRatedon());
                    ps.executeUpdate();
                    added = true;
                }
            } catch (SQLException e) {
                if(e.getSQLState().equals("23505")){
                    System.out.print("Zvol si ine meno:");
                    rating.setPlayer(new Scanner(System.in).nextLine());
                }else{
                    throw new RatingException("Error adding rating..");
                }
            }
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_AVG_RATING)){
                ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        return rs.getInt(1);
                    }else{
                        throw new RatingException("Error loading avg rating");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_RATING)){
                ps.setString(1, player);
                try(ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        Rating ratingFromDB = new Rating(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        return ratingFromDB.getRating();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading rating", e);
        }
        return -1;
    }

    public List<Rating> getAllRatings(String game) throws RatingException{
        List<Rating> ratings = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_RATINGS)){
                ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        Rating ratingFromDB = new Rating(
                                rs.getString(2),
                                rs.getString(1),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        ratings.add(ratingFromDB);
                    }
                    return ratings;
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Error loading ratings", e);
        }
    }

    public int deleteRating(String player) throws RatingException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            try (PreparedStatement ps = connection.prepareStatement(DELETE_RATING)) {
                ps.setString(1, player);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RatingException("Error while deleting rating..");
        }
    }

    public static void main(String[] args) throws Exception {
        //Rating score = new Rating("dzurka", "mines", 4, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        RatingServiceJDBC scoreService = new RatingServiceJDBC();
        System.out.println(scoreService.getAverageRating("cuberoll"));
        //ConsoleUI.getInstance().printAllRatings(scoreService.getAllRatings("mines"), scoreService.getAverageRating("mines"));
    }
}
