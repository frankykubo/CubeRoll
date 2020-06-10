package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.List;

public class ScoreJDBCTest {

    private ScoreServiceJDBC scoreServiceJDBC;

    public ScoreJDBCTest() {
        this.scoreServiceJDBC = new ScoreServiceJDBC();
    }

    @Test
    public void testScoreJDBC() {
        Score score = new Score("test", "testPlayer", 200, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        try {
            System.out.println(scoreServiceJDBC.deleteScore("testPlayer"));
        } catch (ScoreException e) {
            System.out.println("Error while deleting score");
        }
        try {
            scoreServiceJDBC.addScore(score);
            System.out.println("Score added to DB.");
        } catch (ScoreException e) {
            throw new RuntimeException("Failed to add score to DB.");
        }
        List<Score> scores = null;
        try {
            scores = scoreServiceJDBC.getBestScores("test");
            System.out.println("Scoure loaded from DB.");
        } catch (ScoreException e) {
            throw new RuntimeException("Failed to load scores from DB.");
        }
        assertEquals(1, scores.size());
        assertEquals("testPlayer", scores.get(0).getPlayer());
        assertEquals(200, scores.get(0).getPoints());
        assertEquals("test", scores.get(0).getGame());
        System.out.println("Concrete score found in DB.");
        try {
            assertEquals(1, scoreServiceJDBC.deleteScore("testPlayer"));
            System.out.println("Concrete score deleted from DB.");
        } catch (ScoreException e) {
            System.out.println("Error while deleting score.");
        }
    }



}
