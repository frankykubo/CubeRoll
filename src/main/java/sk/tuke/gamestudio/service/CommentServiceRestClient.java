package sk.tuke.gamestudio.service;

import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class CommentServiceRestClient implements CommentService {
    private static Properties properties = ServiceProperties.getServiceProperties();
    private static final String URL = properties.getProperty("rest.url");

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void addComment(Comment comment) throws CommentException {
        restTemplate.postForEntity(URL + "api/comment", comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return Arrays.asList(restTemplate.getForEntity(URL + "api/comment" + "/" + game, Comment[].class).getBody());
    }
}
