package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    public CommentServiceJPA() {
    }

    @Override
    public void addComment(Comment comment) throws CommentException {
        try {
            entityManager.persist(comment);
        }catch (Exception e){
            throw new CommentException("Error adding comment to DB.");
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try {
            return entityManager.createNamedQuery("Comment.getComments", Comment.class)
                    .setParameter("game", game)
                    .setMaxResults(10).getResultList();
        }catch (Exception e){
            throw new CommentException("Error getting comments from DB.");
        }
    }
}
