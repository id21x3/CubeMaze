package sk.tuke.gamestudio.service;

import org.junit.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.rating.RatingService;
import sk.tuke.gamestudio.service.rating.RatingServiceJDBC;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class RatingServiceTest {

    private RatingService createService() {
        return new RatingServiceJDBC();
    }

    @Test
    public void testReset() {
        RatingService service = createService();
        service.reset();
        assertEquals(0, service.getAverageRating("mines"));
    }

    @Test
    public void testSetRating() {
        RatingService service = createService();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("Jaro", "mines", 5, date));

        assertEquals(5, service.getAverageRating("mines"));

        assertEquals(5, service.getRating("mines", "Jaro"));
    }

    @Test
    public void testGetAverageRating() {
        RatingService service = createService();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("Jaro", "mines", 5, date));
        service.setRating(new Rating("Fero", "mines", 3, date));

        assertEquals(4, service.getAverageRating("mines"));
    }

    @Test
    public void testGetRating() {
        RatingService service = createService();
        service.reset();
        Date date = new Date();
        service.setRating(new Rating("Jaro", "mines", 5, date));

        assertEquals(5, service.getRating("mines", "Jaro"));
    }

    @Test
    public void testPersistence() {
        RatingService service = createService();
        service.reset();
        service.setRating(new Rating("Jaro", "mines", 5, new Date()));

        service = createService();
        assertEquals(5, service.getAverageRating("mines"));
    }
}
