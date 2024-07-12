package sk.tuke.backend.gamestudio.service.rating;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.backend.gamestudio.entity.Rating;

@Transactional
public class RatingServiceJPA implements RatingService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            Rating existingRating = (Rating) entityManager.createNamedQuery("Rating.getRatingByPlayerAndGame")
                    .setParameter("player", rating.getPlayer())
                    .setParameter("game", rating.getGame())
                    .getSingleResult();

            existingRating.setRating(rating.getRating());
            existingRating.setRatedOn(rating.getRatedOn());
            entityManager.merge(existingRating);
        } catch (NoResultException e) {
            entityManager.persist(rating);
        } catch (NonUniqueResultException e) {
            throw new RatingException("Multiple ratings found for the same player and game.");
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Double averageRating = (Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game)
                .getSingleResult();
        if (averageRating != null) {
            return (int) Math.round(averageRating);
        } else {
            throw new RatingException("No ratings found for the game.");
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return (int) entityManager.createNamedQuery("Rating.getRating")
                .setParameter("game", game)
                .setParameter("player", player)
                .getSingleResult();
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.reset").executeUpdate();
    }
}
