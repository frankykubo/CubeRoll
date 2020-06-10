package sk.tuke.gamestudio.service;

import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class RatingServiceRestClient implements RatingService {
    private static Properties properties = ServiceProperties.getServiceProperties();
    private static final String URL = properties.getProperty("rest.url");

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(URL + "api/rating", rating, Rating.class);
    }

    public List<Rating> getAllRatings() throws RatingException {
        try {
            return Arrays.asList(restTemplate.getForEntity(URL + "api/rating" + "/all", Rating[].class).getBody());
        }catch (Exception e){
            throw new RatingException("Error occurred when getting all ratings from DB.");
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return restTemplate.getForEntity(String.format("%s/%s", URL + "api/rating", game), Integer.class).getBody();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return restTemplate.getForEntity(String.format("%s/%s/%s", URL + "api/rating", game, player), Integer.class).getBody();
    }
}
