package sk.tuke.gamestudio.game;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import sk.tuke.gamestudio.game.cubeRoll.core.Game;
import sk.tuke.gamestudio.game.cubeRoll.core.Menu;
import sk.tuke.gamestudio.game.cubeRoll.core.TypeOfUI;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.tuke.gamestudio.service.*;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.gamestudio.entity")
public class SpringClient {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }
    @Bean
    public CommandLineRunner runner(Game game) {
        return args -> game.playGame();
    }

    @Bean
    public Game game() {
        return new Game(TypeOfUI.CONSOLE);
    }

    @Bean
    public Menu menu() {
        return new Menu();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceRestClient();
    }
}
