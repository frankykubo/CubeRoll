package sk.tuke.gamestudio.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService{

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            entityManager.persist(rating);
        }catch (Exception e){
            throw new RatingException("Error occurred when adding rating to DB." + e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            return (int)(Math.floor((double)entityManager.createNamedQuery("Rating.getAverageRating").getResultList().get(0)));
        }catch (Exception e){
            throw new RatingException("Error occurred when getting avg rating to DB.");
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            return entityManager.createNamedQuery("Rating.getRating", Integer.class)
                    .setParameter("game", game)
                    .setParameter("name", player)
                    .getSingleResult();
        }catch (NoResultException e) {
            return 0;
        }catch (Exception e){
            throw new RatingException("Error occurred when getting rating from DB." + e);
        }
    }

    public int deleteRating(String game, String player) throws RatingException {
        try {
            return entityManager.createNamedQuery("Rating.deleteRating")
                    .setParameter("game", game)
                    .setParameter("name", player)
                    .executeUpdate();
        }catch (Exception e){
            throw new RatingException("Error occurred when deleting rating from DB." + e);
        }
    }

    public List<Rating> getAllRatings(String game) throws RatingException {
        try {
            return entityManager.createNamedQuery("Rating.getAllRatings", Rating.class).setParameter("game", game).getResultList();
        }catch (Exception e){
            throw new RatingException("Error occurred when getting all ratings from DB.");
        }
    }
}
