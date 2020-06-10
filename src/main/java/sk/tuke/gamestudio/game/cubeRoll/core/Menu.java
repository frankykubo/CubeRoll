package sk.tuke.gamestudio.game.cubeRoll.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;
import sk.tuke.gamestudio.game.cubeRoll.ui.ConsoleUI;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;

import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class Menu {

    @Autowired
    private ScoreService scoreDatabase;

    @Autowired
    private CommentService commentDatabase;

    @Autowired
    private RatingService ratingDatabase;

    public Menu() {
    }

    public int mainMenu(TypeOfUI typeOfUI){
        if(typeOfUI == TypeOfUI.CONSOLE){
            ConsoleUI consoleUI = ConsoleUI.getInstance();
            String input;
            consoleUI.startingScreen();
            while(!(input = consoleUI.getInput()).equals("EXIT")){
                switch (input){
                    case "TOP10":
                        try {
                            consoleUI.printTop10(scoreDatabase.getBestScores("cuberoll")); //TODO PREMENOVAT HRU
                        } catch (ScoreException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "COMMANDS":
                        consoleUI.printCommands();
                        continue;
                    case "COMMENTS":
                        try {
                            consoleUI.printAllComments(commentDatabase.getComments("cuberoll"));
                        } catch (CommentException e) {
                           throw new RuntimeException("Error occured while getting comments from DB.", e);
                        }
                        addComment(TypeOfUI.CONSOLE);
                        consoleUI.startingScreen();
                        break;
                    case "RATING":
                        try {
                            int avg = ratingDatabase.getAverageRating("cuberoll");
                            consoleUI.printAllRatings(((RatingServiceRestClient)ratingDatabase).getAllRatings(), avg);
                        } catch (RatingException e) {
                            throw new RuntimeException("Error ocurred while getting rating from DB.", e);
                        }
                        addRating(TypeOfUI.CONSOLE);
                        consoleUI.startingScreen();
                        break;
                    case "PLAY":
                        return 0;
                    case "LEGEND":
                        consoleUI.printLegend();
                        break;
                    default:
                        consoleUI.handleReturnValue(5);
                }
            }
            return 2;
        }
        throw new RuntimeException("Failure happened in Menu.class.");
    }


    public void addComment(TypeOfUI typeOfUI){
        if(typeOfUI == TypeOfUI.CONSOLE) {
            System.out.println(Color.YELLOW_BOLD + "Do you want to add comment? (yes/no)" + Color.RESET);
            switch (ConsoleUI.getInstance().getInput()) {
                case "YES":
                    break;
                case "NO":
                    return;
                default:
                    ConsoleUI.getInstance().handleReturnValue(5);
                    return;
            }
            Comment comment = new Comment(null, "cuberoll", null, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            System.out.print(Color.YELLOW_BOLD + "Type your name: " + Color.RESET);
            String name = new Scanner(System.in).nextLine();
            System.out.print(Color.YELLOW_BOLD + "Your comment: " + Color.RESET);
            String playersComment = new Scanner(System.in).nextLine();
            comment.setPlayer(name);
            comment.setComment(playersComment);
            try {
                commentDatabase.addComment(comment);
            } catch (CommentException e) {
                throw new RuntimeException("Error adding comment!");
            }
            System.out.println(Color.YELLOW_BOLD + "Comment succesfully added. Returning to main menu." + Color.RESET);
        }
    }

    public void addRating(TypeOfUI typeOfUI){
        if(typeOfUI == TypeOfUI.CONSOLE) {
            System.out.println(Color.YELLOW_BOLD + "Do you want to add rating?" + Color.RESET);
            switch (ConsoleUI.getInstance().getInput()) {
                case "YES":
                    break;
                case "NO":
                    return;
                default:
                    ConsoleUI.getInstance().handleReturnValue(5);
                    return;
            }
            Rating rating = new Rating(null, "cuberoll", 0, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            System.out.print(Color.YELLOW_BOLD + "Type your name: " + Color.RESET);
            String name = new Scanner(System.in).nextLine();
            System.out.print(Color.YELLOW_BOLD + "Your rating <1-5>: " + Color.RESET);
            String playersRating = new Scanner(System.in).nextLine();
            rating.setPlayer(name);
            rating.setRating(Integer.parseInt(playersRating));
            try {
                ratingDatabase.setRating(rating);
            } catch (RatingException e) {
                throw new RuntimeException("Error adding comment to database!");
            }
            System.out.println(Color.YELLOW_BOLD + "Rating succesfully added. Returning to main menu." + Color.RESET);
        }
    }

    public void addScore(TypeOfUI typeOfUI){
        if(typeOfUI == TypeOfUI.CONSOLE) {
            System.out.println(Color.YELLOW_BOLD + "Do you want to put your score into global high score table? (yes/no)" + Color.RESET);
            switch (ConsoleUI.getInstance().getInput()) {
                case "YES":
                    break;
                case "NO":
                    System.out.println(Color.YELLOW_BOLD + "I guess this was not a good day for you. Try again next time, please!" + Color.RESET);
                    return;
                default:
                    ConsoleUI.getInstance().handleReturnValue(5);
                    System.out.println(Color.YELLOW_BOLD + "The game will continue without saving your score." + Color.RESET);
                    return;
            }
            Score score = new Score("cuberoll", null, Game.getScore().getPoints(), new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            System.out.print(Color.YELLOW_BOLD + "Type your name: " + Color.RESET);
            String name = new Scanner(System.in).nextLine();
            score.setPlayer(name);
            try {
                scoreDatabase.addScore(score);
            } catch (ScoreException e) {
                throw new RuntimeException("Error adding score to database!");
            }
            System.out.println(Color.YELLOW_BOLD + "Score succesfully added. Thank you for playing!!." + Color.RESET);
        }
    }

}
