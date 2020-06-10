package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.cubeRoll.core.*;
import sk.tuke.gamestudio.service.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/cuberoll")
public class CubeRollController {
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    private Game game;
    private static Score score;
    private int state;
    private Level actualLevel;
    private static int level;
    private LevelFactory factory = new LevelFactory();

    @RequestMapping
    public String cuberoll(String direction, Model model) {
        if (game == null)
            newGame();
        try {
            if(direction != null) {
                state = actualLevel.play(direction);
                switch (state) {
                    case 0:
                        break;
                    case 1:
                        actualLevel.fallToTheHole();
                        break;
                    case 5:
                        model.addAttribute("levelSteps", score.getActualSteps());
                        model.addAttribute("finishedLevel", level);
                        level++;
                        if(level == 11){
                            score.updatePoints();
                            state=10; //Finished
                            prepareModel(model);
                            return "cuberoll";
                        }
                        if(state == 5)score.updatePoints();
                        actualLevel = factory.createLevel(level, TypeOfUI.GRAPHICAL);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        prepareModel(model);
        return "cuberoll";
    }

    @RequestMapping("/new")
    public String newGame(Model model) {
        newGame();
        state = 0;
        prepareModel(model);
        return "cuberoll";
    }

    @RequestMapping("/restart")
    public String restart(Model model) {
        if(actualLevel == null){
            newGame(model);
        }
        state = 0;
        prepareModel(model);
        return "cuberoll";
    }

    @RequestMapping("/userRestart")
    public String userRestart(Model model) {
        if(actualLevel == null){
            return "menu";
        }
        actualLevel.resetLevelByPlayer();
        state = 0;
        prepareModel(model);
        return "cuberoll";
    }

    public String printHtmlMap() {
        StringBuilder sb = new StringBuilder();
        //MAP
        sb.append("<div class=\"map\" id=\"mapElm\">");
        for(int vyska=0; vyska<actualLevel.getRows(); vyska++){
            for(int sirka=0; sirka<actualLevel.getColumns(); sirka++){
                if(vyska == actualLevel.getCube().getPositionY() && sirka == actualLevel.getCube().getPositionX()){
                    sb.append("<div class=\"tooltip\">");
                    if(actualLevel.getCube().getActualSide() == actualLevel .getCube().getFinishPos()) {
                        sb.append("<img src='/images/cuberoll/cubeYellow.jpg'>");
                    }else{
                        sb.append("<img src='/images/cuberoll/cubeBlue.jpg'>");
                    }
                    sb.append("<span class=\"tooltiptext\">Tu sa momentálne nachádza vaša kocka</span></div>");
                    sb.append("\n");
                }else {
                    sb.append("<div class=\"tooltip\">");
                    sb.append("<img src='/images/cuberoll/" + actualLevel.getMap()[vyska][sirka].getImageName() + "'>");
                    sb.append("<span class=\"tooltiptext\">" + actualLevel.getMap()[vyska][sirka].getTooltipText() +"</span></div>");
                    sb.append("\n");
                    //"<div class=\"tooltip\"><span class=\"tooltiptext\">" + actualLevel.getMap()[vyska][sirka].getImageName() + "</span></div>" + "'>"
                }
            }
            sb.append("<br>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    public String printHtmlCube(){
        StringBuilder sb = new StringBuilder();
        int[][] cubeSides = actualLevel.getCube().getCubeSides();
        sb.append("<div class=\"nextMove\">Next move</div>");
        sb.append("<div class=\"cubeRot\">");
        sb.append("<img src='/images/cuberoll/hole.jpg'>\n");
        sb.append("<a href='/cuberoll?direction=UP'>\n");
        if(cubeSides[0][2] == actualLevel.getCube().getFinishPos()){
            sb.append("<img src='/images/cuberoll/cubeUpYellow.jpg'></a>\n");
        }else {
            sb.append("<img src='/images/cuberoll/cubeUpBlue.jpg'></a>\n");
        }
        sb.append("<img src='/images/cuberoll/hole.jpg'>\n<br>\n");
        for (int row = 1; row < 4; row++){
            if(row == 2){
                if(cubeSides[1][2] == actualLevel.getCube().getFinishPos()){
                    sb.append("<img src='/images/cuberoll/cubeYellow.jpg'>\n");
                }
                else {
                    sb.append("<img src='/images/cuberoll/cubeBlue.jpg'>\n");
                }
            }else{
                if(cubeSides[1][row] == actualLevel.getCube().getFinishPos()){
                    if(row == 1){
                        sb.append("<a href='/cuberoll?direction=LEFT'>\n");
                        sb.append("<img src='/images/cuberoll/cubeLeftYellow.jpg'></a>\n");
                    }else{
                        sb.append("<a href='/cuberoll?direction=RIGHT'>\n");
                        sb.append("<img src='/images/cuberoll/cubeRightYellow.jpg'></a>\n<br>\n");
                    }
                }
                else {
                    if(row == 1) {
                        sb.append("<a href='/cuberoll?direction=LEFT'>\n");
                        sb.append("<img src='/images/cuberoll/cubeLeftBlue.jpg'></a>\n");
                    }else{
                        sb.append("<a href='/cuberoll?direction=RIGHT'>\n");
                        sb.append("<img src='/images/cuberoll/cubeRightBlue.jpg'></a>\n<br>\n");
                    }
                }
            }
        }
        sb.append("<img src='/images/cuberoll/hole.jpg'>\n");
        sb.append("<a href='/cuberoll?direction=DOWN'>\n");
        if(cubeSides[2][2] == actualLevel.getCube().getFinishPos()){
            sb.append("<img src='/images/cuberoll/cubeDownYellow.jpg'></a>\n");
        }
        else{
            sb.append("<img src='/images/cuberoll/cubeDownBlue.jpg'></a>\n");
        }
        sb.append("<img id=\"footer\" src='/images/cuberoll/hole.jpg'>\n<br>\n");
        sb.append("</div>");
        return sb.toString();
    }

    public static int getLevel() {
        return level;
    }

    @RequestMapping("/rating")
    public String rating(Model model, String userName, String rating) {
        if(actualLevel == null){
            return "menu";
        }
        if (rating != null && userName !=null) {
                try {
                    int value = ratingService.getRating("cuberoll", userName);
                    if(value == 0) {
                        ratingService.setRating(new Rating(userName, "cuberoll", Integer.parseInt(rating), new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
                    }else{
                        ((RatingServiceJPA)ratingService).deleteRating("cuberoll", userName);
                        ratingService.setRating(new Rating(userName, "cuberoll", Integer.parseInt(rating), new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
                    }
                    prepareModel(model);
                    return "cuberoll";
                } catch (RatingException e) {
                    e.printStackTrace();
                }
        }
        prepareModel(model);
        return "cuberoll";
    }

    @RequestMapping("/comment")
    public String comment(Model model, String userName, String comment) {
        if(actualLevel == null){
            return "menu";
        }
        if (comment != null && userName !=null) {
            try {
                commentService.addComment(new Comment(userName, "cuberoll", comment, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
                prepareModel(model);
                return "cuberoll";
            } catch (CommentException e) {
                e.printStackTrace();
            }
        }
        prepareModel(model);
        return "cuberoll";
    }

    @RequestMapping("/score")
    public String score(Model model, String userName) {
        if(actualLevel == null){
            return "menu";
        }
        int score = getScore().getPoints();
        score++;
        score++;
        if (score != 0 && userName !=null) {
            try {
                Score scoreFromDB = ((ScoreServiceJPA)scoreService).getScore("cuberoll", userName); //TODO
                if(scoreFromDB == null) {
                    scoreService.addScore(new Score("cuberoll", userName, score, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
                }else{
                    if(score > scoreFromDB.getPoints()){
                        ((ScoreServiceJPA)scoreService).deleteScore("cuberoll", userName);
                        scoreService.addScore(new Score("cuberoll", userName, score, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
                        model.addAttribute("lowerScoreThanPersonalBest", false);
                        state = 11;
                    }else{
                        model.addAttribute("lowerScoreThanPersonalBest", true);
                        state = 11;
                    }
                }
                prepareModel(model);
                return "cuberoll";
            } catch (ScoreException e) {
                e.printStackTrace();
            }
        }
        prepareModel(model);
        return "cuberoll";
    }

    private void prepareModel(Model model) {
        try {
            List<Score> scores = scoreService.getBestScores("cuberoll");
            List<Comment> comments = commentService.getComments("cuberoll");
            List<Rating> ratings = ((RatingServiceJPA)ratingService).getAllRatings("cuberoll");
            model.addAttribute("scores", scores);
            model.addAttribute("comments", comments);
            model.addAttribute("ratings", ratings);
            model.addAttribute("ratingsSize", ratings.size());
            model.addAttribute("commentsSize", comments.size());
            model.addAttribute("scoresSize", scores.size());
            model.addAttribute("avgRating", ratingService.getAverageRating("cuberoll"));
        } catch (ScoreException | RatingException | CommentException e) {
            e.printStackTrace();
        }
    }

    private void newGame() {
        game = new Game(TypeOfUI.GRAPHICAL);
        level = 1;
        score = new Score();
        actualLevel = factory.createLevel(level, TypeOfUI.GRAPHICAL);
    }

    public static Score getScore() {
        return score;
    }

    public int getState() {
        return state;
    }

    public Level getActualLevel() {
        return actualLevel;
    }

    public void setStateTo0() {
        this.state = 0;
    }

    public String getDate(Timestamp str2Check){
        String timestampString = str2Check.toString();
        Pattern checkRegex = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
        Matcher regexMatcher = checkRegex.matcher( timestampString );
        if(!regexMatcher.find()) throw new IllegalArgumentException("Bad date.");
        return regexMatcher.group().trim();
    }
}
