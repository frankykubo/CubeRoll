package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreException;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreRestService {
    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        try {
            return scoreService.getBestScores(game);
        } catch (ScoreException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping
    public void addScore(@RequestBody Score score) {
        try {
            scoreService.addScore(score);
        } catch (ScoreException e) {
            e.printStackTrace();
        }
    }
}
