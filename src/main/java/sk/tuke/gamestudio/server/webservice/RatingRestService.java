package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.*;

import java.util.List;

@RestController
@RequestMapping("/api/rating")
public class RatingRestService {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public void setRating(@RequestBody Rating rating) {
        try {
            ratingService.setRating(rating);
        } catch (RatingException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/{game}")
    public int getAverageRating(@PathVariable String game) {
        try {
            return ratingService.getAverageRating(game);
        } catch (RatingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @GetMapping("{/{game}/{player}}")
    public int getRating(@PathVariable String game,@PathVariable String player) {
        try {
            return ratingService.getRating(game, player);
        } catch (RatingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @GetMapping("/all")
    public List<Rating> getAllRatings() {
        try {
            return ((RatingServiceJPA)ratingService).getAllRatings("cuberoll");
        } catch (RatingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
