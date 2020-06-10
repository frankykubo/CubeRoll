package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentJDBCTest {
    private CommentServiceJDBC commentServiceJDBC;

    public CommentJDBCTest() {
        this.commentServiceJDBC = new CommentServiceJDBC();
    }

    @Test
    public void testCommentJDBC() {
        Comment comment = new Comment("testPlayer", "test", "testComment", new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        try {
            commentServiceJDBC.deleteComment("testPlayer");
        } catch (CommentException e) {
            System.out.println("Error while deleting score");
        }
        try {
            commentServiceJDBC.addComment(comment);
            System.out.println("Added comment.");
        } catch (CommentException e) {
            throw new RuntimeException("Failed to add score to DB.");
        }
        List<Comment> comments = null;
        try {
            comments = commentServiceJDBC.getComments("test");
            System.out.println("Succesfully loaded comment.");
        } catch (CommentException e) {
            throw new RuntimeException("Failed to load scores from DB.");
        }
        assertEquals(1, comments.size());
        assertEquals("testPlayer", comments.get(0).getPlayer());
        assertEquals("testComment", comments.get(0).getComment());
        assertEquals("test", comments.get(0).getGame());
        System.out.println("Concrete comment found in database.");
        try {
            assertEquals(1, commentServiceJDBC.deleteComment("testPlayer"));
            System.out.println("Concrete comment removed from database.");
        } catch (CommentException e) {
            System.out.println("Error while deleting score.");
        }
    }
}
