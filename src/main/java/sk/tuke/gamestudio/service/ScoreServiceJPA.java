package sk.tuke.gamestudio.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {
        @PersistenceContext
        private EntityManager entityManager;

        @Override
        public void addScore(Score score) throws ScoreException {
            try {
                entityManager.persist(score);
            }catch (Exception e) {
                throw new ScoreException("Error adding score.");
            }
        }

        @Override
        public List<Score> getBestScores(String game) throws ScoreException {
            try {
                return entityManager.createNamedQuery("Score.getBestScores", Score.class).setParameter("game", game).setMaxResults(10).getResultList();
            }catch (Exception e){
                throw new ScoreException("Error loading best scores.");
            }
        }

        public Score getScore(String game, String player) throws ScoreException {
            try {
                return entityManager.createNamedQuery("Score.getScore", Score.class).setParameter("game", game).setParameter("player", player).getSingleResult();
            }catch(NoResultException e) {
                return null;
            }catch(Exception e){
                throw new ScoreException("Error loading best scores.");
            }
        }

        public int deleteScore(String game, String player) throws ScoreException {
            try {
                return entityManager.createNamedQuery("Score.deleteScore")
                        .setParameter("game", game)
                        .setParameter("name", player)
                        .executeUpdate();
            }catch (Exception e){
                throw new ScoreException("Error occurred when deleting score from DB." + e);
            }
        }
}
