package sk.tuke.gamestudio.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Objects;

@RestController
public class RatingServiceRestClient implements RatingService{
    @Value("${remote.server.api}/rating")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return Objects.requireNonNull(restTemplate.getForObject(url + "/" + game, Integer.class));
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return Objects.requireNonNull(restTemplate.getForObject(url + "/" + game + "/" + player, Integer.class));
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
