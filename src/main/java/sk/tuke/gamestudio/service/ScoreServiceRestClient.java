package sk.tuke.gamestudio.service;

import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ScoreServiceRestClient implements ScoreService {
    private static Properties properties = ServiceProperties.getServiceProperties();
    private static final String URL = properties.getProperty("rest.url");

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addScore(Score score) {
        restTemplate.postForEntity(URL + "api/score", score, Score.class);
    }

    @Override
    public List<Score> getBestScores(String gameName) {
        return Arrays.asList(restTemplate.getForEntity(URL +"api/score" + "/" + gameName, Score[].class).getBody());
    }
}
