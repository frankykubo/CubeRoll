package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import java.sql.*;
import java.util.*;

public class ScoreServiceJDBC implements ScoreService {
    private static Properties properties = ServiceProperties.getServiceProperties();
    private static final String URL = properties.getProperty("jdbc.url");
    private static final String USER = properties.getProperty("jdbc.username");
    private static final String PASSWORD = properties.getProperty("jdbc.password");
    private static final String INSERT_SCORE = properties.getProperty("jdbc.score.INSERT_SCORE");
    private static final String SELECT_SCORE = properties.getProperty("jdbc.score.SELECT_SCORE");
    private final static String DELETE_SCORE = properties.getProperty("jdbc.score.DELETE_SCORE");

    @Override
    public void addScore(Score score) throws ScoreException {
        if(score.getPoints() < 1){
            throw new IllegalArgumentException("Points are zero or below, what is forbidden parameter!");
        }
        boolean added = false;
        while(!added) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                try (PreparedStatement ps = connection.prepareStatement(INSERT_SCORE)) {
                    ps.setString(1, score.getGame());
                    ps.setString(2, score.getPlayer());
                    ps.setInt(3, score.getPoints());
                    ps.setTimestamp(4, score.getPlayedOn());
                    ps.executeUpdate();
                    added = true;
                }
            } catch (SQLException e) {
                if(e.getSQLState().equals("23505")){
                    System.out.print("Zvol si ine meno:");
                    score.setPlayer(new Scanner(System.in).nextLine());
                }else{
                    throw new ScoreException("Error adding score..");
                }
            }
        }
    }

    @Override
    public List<Score> getBestScores(String game) throws ScoreException {
        List<Score> scores = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_SCORE)){
                ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()) {
                        Score score = new Score(
                                rs.getString(1),
                                rs.getString(2),
                                rs.getInt(3),
                                rs.getTimestamp(4)
                        );
                        scores.add(score);
                    }
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Error loading score", e);
        }
        return scores;
    }

    public int deleteScore(String player) throws ScoreException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            try (PreparedStatement ps = connection.prepareStatement(DELETE_SCORE)) {
                ps.setString(1, player);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ScoreException("Error deleting score..");
        }
    }

    public static void main(String[] args) throws Exception {
        //Score score = new Score("mines", "TestCasu", 1005, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        ScoreService scoreService = new ScoreServiceJDBC();
        //scoreService.addScore(score);
        System.out.println(scoreService.getBestScores("cuberoll"));
        //ConsoleUI.getInstance().printTop10(scoreService.getBestScores("mines"));
        Properties properties = ServiceProperties.getServiceProperties();
        System.out.println(properties.getProperty("jdbc.url"));
    }
}
