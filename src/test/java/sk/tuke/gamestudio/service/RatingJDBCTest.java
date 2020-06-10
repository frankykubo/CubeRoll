package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingServiceJDBC;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingJDBCTest {
    private RatingServiceJDBC ratingServiceJDBC;

    public RatingJDBCTest() {
        this.ratingServiceJDBC = new RatingServiceJDBC();
    }

    @Test
    public void testRatingJDBC() {
        Rating rating = new Rating("testPlayer", "test", 4, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        try {
            ratingServiceJDBC.deleteRating("testPlayer");
        } catch (RatingException e) {
            System.out.println("Error while deleting rating");
        }
        try {
            ratingServiceJDBC.setRating(rating);
            System.out.println("Rating added to database.");
        } catch (RatingException e) {
            throw new RuntimeException("Failed to add rating to DB.");
        }
        List<Rating> ratings = null;
        try {
            ratings = ratingServiceJDBC.getAllRatings("test");
            System.out.println("Ratings succesfully loaded from DB.");
        } catch (RatingException e) {
            throw new RuntimeException("Failed to load ratings from DB.");
        }
        assertEquals(1, ratings.size());
        assertEquals("testPlayer", ratings.get(0).getPlayer());
        assertEquals(4, ratings.get(0).getRating());
        assertEquals("test", ratings.get(0).getGame());
        System.out.println("Concrete rating found in DB.");
        try {
            assertEquals(1, ratingServiceJDBC.deleteRating("testPlayer"));
            System.out.println("Concrete rating removed from DB.");
        } catch (RatingException e) {
            System.out.println("Error while deleting score.");
        }
    }
}
