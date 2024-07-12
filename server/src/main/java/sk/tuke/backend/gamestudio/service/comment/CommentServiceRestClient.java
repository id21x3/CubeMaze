package sk.tuke.backend.gamestudio.service.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sk.tuke.backend.gamestudio.entity.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
public class CommentServiceRestClient implements CommentService{
    @Value("${remote.server.api}/comment")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) throws CommentException {
        restTemplate.postForEntity(url, comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(url + "/" + game, Comment[].class).getBody()));
    }

    @Override
    public Comment getLastCommentFromPlayer(String player) throws CommentException {
        return restTemplate.getForEntity(url + "/lastComment/" + player, Comment.class).getBody();
    }

    @Override
    public void reset() throws CommentException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
