package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.game.cubeRoll.ui.ConsoleUI;
import sk.tuke.gamestudio.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class CommentServiceJDBC implements CommentService {

    private static Properties properties = ServiceProperties.getServiceProperties();
    private static final String URL = properties.getProperty("jdbc.url");
    private static final String USER = properties.getProperty("jdbc.username");
    private static final String PASSWORD = properties.getProperty("jdbc.password");
    private static final String INSERT_COMMENT = properties.getProperty("jdbc.comment.INSERT_COMMENT");
    private static final String SELECT_ALL_COMMENTS = properties.getProperty("jdbc.comment.SELECT_ALL_COMMENTS");
    private final static String DELETE_COMMENT = properties.getProperty("jdbc.comment.DELETE_COMMENT");

    @Override
    public void addComment(Comment comment) throws CommentException {
        if(comment.getComment() == null){
            throw new IllegalArgumentException("Comment is NULL!");
        }
        boolean added = false;
        while(!added) {
            try {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                try (PreparedStatement ps = connection.prepareStatement(INSERT_COMMENT)) {
                    ps.setString(1, comment.getGame());
                    ps.setString(2, comment.getPlayer());
                    ps.setString(3, comment.getComment());
                    ps.setTimestamp(4, comment.getCommentedOn());
                    ps.executeUpdate();
                    added = true;
                }
            } catch (SQLException e) {
                if(e.getSQLState().equals("23505")){
                    System.out.print("Zvol si ine meno:");
                    comment.setPlayer(new Scanner(System.in).nextLine());
                }else
                    throw new CommentException("Error adding comment", e);
            }
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try(PreparedStatement ps = connection.prepareStatement(SELECT_ALL_COMMENTS)){
                ps.setString(1, game);
                try(ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        Comment commentFromDB = new Comment(
                                rs.getString(2),
                                rs.getString(1),
                                rs.getString(3),
                                rs.getTimestamp(4)
                        );
                        comments.add(commentFromDB);
                    }
                    return comments;
                }
            }
        } catch (SQLException e) {
            throw new CommentException("Error loading comments", e);
        }
    }

    public int deleteComment(String player) throws CommentException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            try (PreparedStatement ps = connection.prepareStatement(DELETE_COMMENT)) {
                ps.setString(1, player);
                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new CommentException("Error deleting comment..");
        }
    }

    public static void main(String[] args) throws Exception {
        //Comment score = new Comment("vyliatepivo", "mines", "Ta uvidime ci to funguje no...", new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        CommentServiceJDBC scoreService = new CommentServiceJDBC();
        //scoreService.addComment(score);
        ConsoleUI.getInstance().printAllComments(scoreService.getComments("cuberoll"));
    }
}
