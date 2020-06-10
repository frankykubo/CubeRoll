package sk.tuke.gamestudio.game.cubeRoll.ui;

import sk.tuke.gamestudio.game.cubeRoll.core.Game;
import sk.tuke.gamestudio.game.cubeRoll.core.Level;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ConsoleUI extends UI {
    private static ConsoleUI INSTANCE;
    private Level level;
    private int scoreHelper;

    private ConsoleUI() {
        scoreHelper = 0;
    }

    public static ConsoleUI getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ConsoleUI();
        }
        return INSTANCE;
    }

    public void printCube(){
        int[][] cubeSides = level.getCube().getCubeSides();
        System.out.print(Color.BLACK_BACKGROUND + " Cube Rotation " + Color.RESET);
        System.out.println();
        System.out.print(Color.BLACK_BACKGROUND + "   " + Color.RESET + "   ");
        if(cubeSides[0][2] == level.getCube().getFinishPos()){
            System.out.print(Color.BLACK);
            System.out.print(Color.YELLOW_BACKGROUND);
            System.out.print(" \u2191 " + Color.RESET);
        }else {
            System.out.print(Color.BLACK);
            System.out.print(Color.BLUE_BACKGROUND);
            System.out.print(" \u2191 " + Color.RESET);
        }
        System.out.print("   " + Color.BLACK_BACKGROUND + "   " + Color.RESET);
        System.out.println();
        System.out.print(Color.BLACK_BACKGROUND + "   " + Color.RESET);
        for (int row = 1; row < 4; row++){
            if(row == 2){
                if(cubeSides[1][2] == level.getCube().getFinishPos()){
                    System.out.print(Color.BLACK);
                    System.out.print(Color.YELLOW_BACKGROUND);
                    System.out.print("[ ]" + Color.RESET);
                }
                else {
                    System.out.print(Color.BLACK);
                    System.out.print(Color.BLUE_BACKGROUND);
                    System.out.print("[ ]" + Color.RESET);
                }
            }else{
                if(cubeSides[1][row] == level.getCube().getFinishPos()){
                    System.out.print(Color.BLACK);
                    System.out.print(Color.YELLOW_BACKGROUND);
                    if(row == 1){
                        System.out.print(" \u2190 " + Color.RESET);
                    }else{
                        System.out.print(" \u2192 " + Color.RESET);
                    }
                }
                else {
                    System.out.print(Color.BLACK);
                    System.out.print(Color.BLUE_BACKGROUND);
                    if(row == 1) {
                        System.out.print(" \u2190 " + Color.RESET);
                    }else{
                        System.out.print(" \u2192 " + Color.RESET);
                    }
                }
            }
        }
        System.out.print(Color.BLACK_BACKGROUND + "   " + Color.RESET);
        System.out.println();
        System.out.print(Color.BLACK_BACKGROUND + "   " + Color.RESET + "   ");
        if(cubeSides[2][2] == level.getCube().getFinishPos()){
            System.out.print(Color.BLACK);
            System.out.print(Color.YELLOW_BACKGROUND);
        }
        else{
            System.out.print(Color.BLACK);
            System.out.print(Color.BLUE_BACKGROUND);
        }
        System.out.print(" \u2193 " + Color.RESET);
        System.out.print("   " + Color.BLACK_BACKGROUND + "   " + Color.RESET);
        System.out.println();
        System.out.print(Color.BLACK_BACKGROUND + "               " + Color.RESET);
        System.out.println();
    }

    public void printStep(){
        scoreHelper = 0;
        int charCounter = 0;
        for(int ciara=0; ciara < level.getColumns(); ciara++) {
            if(ciara == (level.getColumns()/3)){
                System.out.print(Color.BLACK_UNDERLINED);
                System.out.print(Color.RED_BACKGROUND + "Level "+Game.getLevel()+" actual map and score" + Color.RESET);
                charCounter += 28;
            }
            else{
                System.out.print(Color.BLACK_UNDERLINED);
                System.out.print(Color.RED_BACKGROUND + "   " + Color.RESET);
                charCounter += 3;
            }
        }
        System.out.println();
        for(int vyska = 0; vyska<level.getRows(); vyska++){
            for(int sirka = 0; sirka<level.getColumns(); sirka++){
                if(vyska == level.getCube().getPositionY() && sirka == level.getCube().getPositionX()){
                    if(level.getCube().getActualSide() == level.getCube().getFinishPos()) {
                        System.out.print(Color.BLACK);
                        System.out.print(Color.YELLOW_BACKGROUND + "[ ]" + Color.RESET); //#[]#
                    }else{
                        System.out.print(Color.BLACK);
                        System.out.print(Color.BLUE_BACKGROUND + "[ ]" + Color.RESET); //#[]#
                    }
                }else {
                    System.out.print(level.getMap()[vyska][sirka].printSquare());
                }
            }
            if(scoreHelper == 0){
                System.out.println("   Steps: " + level.getScore().getActualSteps());
                if(level.getRows() == 1){ //pre pripad ze by mal level 1 riadok
                    for(int i = 0; i < level.getColumns()*3; i++) System.out.print(" ");
                    System.out.println("   Deaths: " + level.getScore().getDeaths());
                }
                scoreHelper++;
            }else if(scoreHelper == 1){
                System.out.print("   Deaths: " + level.getScore().getDeaths());
                if(level.getRows() > 2) System.out.println();
                scoreHelper++;
            }else{
                if(vyska+1<level.getRows()) System.out.println();
            }
        }
        System.out.println();
        for(int ciara=0; ciara < charCounter; ciara++) {
            System.out.print(Color.BLACK);
            System.out.print(Color.RED_BACKGROUND + "*" + Color.RESET);
        }
        System.out.println();
    }

    public String getInput(){
        System.out.print(Color.MAGENTA_UNDERLINED + "Waiting for input:" + Color.RESET + " ");
        String input = new Scanner(System.in).nextLine();
        return regexChecker(input);
    }

    public void handleReturnValue(int value){
        switch (value) {
            case 0:
                System.out.println(Color.YELLOW_BOLD + "Congratulations. You were successfull and nailed level " + Game.getLevel() +"!" + Color.RESET);
                return;
            case 1:
                System.out.println(Color.YELLOW_BOLD + "You've fallen into the hole. Try again!" + Color.RESET);
                return;
            case 5:
                System.out.println(Color.YELLOW_BOLD + "Bad input. It is case-insensitive but you have to write exact commands." + Color.RESET);
                System.out.println(Color.YELLOW_BOLD + "Write \"commands\" to view possible commands." + Color.RESET);
        }

    }

    private String regexChecker(String str2Check){
        Pattern checkRegex = Pattern.compile("^[ ]*[u](p)?[ ]*$|^[ ]*[d](own)?[ ]*$|^[ ]*[r](ight)?[ ]*$|^[ ]*[l](eft)?[ ]*$|^[ ]*no[ ]*$|^yes$|^top10$|^comments$|^rating$|^play$|^reset$|^exit$|^commands$|^legend$", Pattern.CASE_INSENSITIVE);
        Matcher regexMatcher = checkRegex.matcher( str2Check );
        if(!regexMatcher.find()) return "BAD INPUT";
        String input = regexMatcher.group().trim().toUpperCase();
        switch (input){
            case "U": return "UP";
            case "D": return "DOWN";
            case "R": return  "RIGHT";
            case "L": return "LEFT";
        }
        return input;
    }

    public void startingScreen(){
        for(int i = 0; i < 5; i++){
            for(int q = 0; q < 21; q++){
                if(i == 2 && q == 7){
                    //System.out.print(Color.WHITE);
                    System.out.print(Color.YELLOW_BACKGROUND);
                    System.out.print(Color.BLACK_BOLD + "WELCOME TO CUBE ROLL!" + Color.RESET);
                    q = q+6;
                }else{
                    if(i == 4){
                        System.out.print(Color.BLACK);
                        System.out.print(Color.YELLOW_BACKGROUND + " Type \"play\" to start the game or \"commands\" to view commands. " + Color.RESET);
                        q = 21;
                    }else
                        System.out.print(Color.YELLOW_BACKGROUND + "   " + Color.RESET);
                }
            }
            System.out.println();
        }
    }

    public void printCommands(){
        System.out.print(Color.YELLOW_BACKGROUND);
        System.out.print(Color.BLACK_BOLD + "                      Main menu                               Game               " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println();
        //top10$|^comments$|^rating$|^play$|^exit$|^commands
        System.out.println(Color.YELLOW_BACKGROUND + "                 play = start game                    u or up = move up          " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println(Color.YELLOW_BACKGROUND + "      top10 = view top 10 players / add comment     d or down = move down        " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println(Color.YELLOW_BACKGROUND + "      comments = view all comments / add comment   r or right = move right       " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println(Color.YELLOW_BACKGROUND + "            rating = view / add rating              l or left = move left        " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println(Color.YELLOW_BACKGROUND + "                       -                             reset = reset level         " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println(Color.YELLOW_BACKGROUND + "             commands = view commands             commands = view commands       " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println(Color.YELLOW_BACKGROUND + "               legend = show legend                  legend = show legend        " + Color.RESET);
        System.out.print(Color.BLACK);
        System.out.println(Color.YELLOW_BACKGROUND + "                 exit = exit game                  exit = exit to main menu      " + Color.RESET);
    }

    public void printLegend(){
        System.out.print(Color.WHITE_BACKGROUND + "   " + Color.RESET);
        System.out.println(" - A classic square, you can use it how many times you want and without any limitations.");
        System.out.println();
        System.out.print(Color.YELLOW_BACKGROUND + "   " + Color.RESET + " or " + Color.BLUE_BACKGROUND + "   " + Color.RESET);
        System.out.println(" - The classic square, but the color of the down side of cube has to match the color of this square.");
        System.out.println();
        System.out.print(Color.BLACK);
        System.out.print(Color.YELLOW_BACKGROUND + " F " + Color.RESET);
        System.out.println(" - A finish. The color of down side of cube has to be yellow if you want to end the level!");
        System.out.println();
        System.out.print(Color.BLACK);
        System.out.print(Color.WHITE_BACKGROUND + " T " + Color.RESET);
        System.out.println(" - High ground. This square is one level higher than all the others, so you will rotate twice.");
        System.out.println();
        System.out.print(Color.BLACK_BACKGROUND + "   " + Color.RESET);
        System.out.println(" - A hole. The level is restarted if you hop on this square and the number of deaths is increased.");
        System.out.println();
        System.out.print(Color.BLACK);
        System.out.print(Color.WHITE_BACKGROUND + " \u2195 " + Color.RESET);
        System.out.println(" - Ice. You will continue to move on the way you hopped on this square until some other type of the square is reached.");
        System.out.println();
        System.out.print(Color.BLACK);
        System.out.print(Color.WHITE_BACKGROUND + " \u2191 " + Color.RESET);
        System.out.print(" or ");
        System.out.print(Color.BLACK);
        System.out.print(Color.WHITE_BACKGROUND + " \u2193 " + Color.RESET);
        System.out.print(" or ");
        System.out.print(Color.BLACK);
        System.out.print(Color.WHITE_BACKGROUND + " \u2190 " + Color.RESET);
        System.out.print(" or ");
        System.out.print(Color.BLACK);
        System.out.print(Color.WHITE_BACKGROUND + " \u2192 " + Color.RESET);
        System.out.println(" - Moving square. Basically moves you the way marked on it.");
        System.out.println();
        System.out.print(Color.BLACK);
        System.out.print(Color.GREEN_BACKGROUND + " P " + Color.RESET);
        System.out.println(" - A push button. When you hop on this square, there will appear the hidden square on the map.");
        System.out.println();
        System.out.print(Color.BLACK);
        System.out.print(Color.GREEN_BACKGROUND + " X " + Color.RESET);
        System.out.println(" - A temporary square. X is a number that specifies, how many times you can use this square.");
        System.out.println();
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void printTop10(List<Score> scores){
        int rank = 1;
        System.out.print(Color.YELLOW_BACKGROUND);
        System.out.print(Color.BLACK_BOLD + "   Rank           Player                Points          Date and time          " + Color.RESET);
        System.out.println();
        for(Score score : scores){
            System.out.print(Color.BLACK);
            System.out.print(Color.YELLOW_BACKGROUND);
            if(rank == 10){
                System.out.print("   " + rank + ".");
            }else
            System.out.print("    " + rank + ".");
            System.out.print("            "+score.getPlayer());
            for(int i = score.getPlayer().length(); i<22; i++){
                System.out.print(" ");
            }
            System.out.print(score.getPoints());
            for(int i = (int) (Math.log10(score.getPoints()) + 1); i < 16; i++) {
                System.out.print(" ");
            }
            System.out.print(dateChecker(score.getPlayedOn().toString()));
            System.out.print("    ");
            System.out.print(Color.RESET);
            System.out.println();
            rank++;
        }
    }

    public void printAllRatings(List<Rating> ratings, int average){
        System.out.print(Color.YELLOW_BACKGROUND);
        System.out.print(Color.BLACK_BOLD + "  "+"\u2606"+"Player"+"\u2606"+"          "+"\u2606"+"Rating"+"\u2606"+"           "+"Date and time       " + Color.RESET);
        System.out.println();
        for(Rating rating : ratings){
            System.out.print(Color.BLACK);
            System.out.print(Color.YELLOW_BACKGROUND);
            System.out.print("   ");
            System.out.print(rating.getPlayer());
            for(int i = rating.getPlayer().length(); i < 18; i++){
                System.out.print(" ");
            }
            for(int i=0; i < rating.getRating(); i++){
                System.out.print("\u2605");
            }
            for(int i = rating.getRating(); i<5; i++){
                System.out.print("\u2606");
            }
            for(int i = 6; i < 14; i++){
                System.out.print(" ");
            }
            System.out.print(dateChecker(rating.getRatedon().toString()));
            System.out.print("     ");
            System.out.print(Color.RESET);
            System.out.println();
        }
        System.out.print(Color.BLACK);
        System.out.print(Color.MAGENTA_BACKGROUND + "                  AVERAGE RATING IS ");
        for(int i = 0; i < average; i++){
            System.out.print("\u2605");
        }
        for(int i = average; i<5; i++){
            System.out.print("\u2606");
        }
        System.out.print("                 ");
        System.out.println(Color.RESET);
    }

    public void printAllComments(List<Comment> comments){
        System.out.print(Color.YELLOW_BACKGROUND);
        System.out.println(Color.BLACK_BOLD + "ALL COMMENTS SORTED BY NEWEST DATE" + Color.RESET);
        for(Comment comm : comments) {
            if(comm.getComment() != null && comm.getPlayer() != null && comm.getCommentedOn() != null) {
                System.out.print("Player " + Color.GREEN_BOLD + comm.getPlayer() + Color.RESET + " on " + dateChecker(comm.getCommentedOn().toString()) + Color.WHITE_BOLD + " commented: " + Color.GREEN + comm.getComment() + Color.RESET);
                System.out.println();
                System.out.println();
            }
        }
    }

    private static String dateChecker(String str2Check){
        Pattern checkRegex = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}");
        Matcher regexMatcher = checkRegex.matcher( str2Check );
        if(!regexMatcher.find()) throw new IllegalArgumentException("Bad date.");
        return regexMatcher.group().trim();
    }
}
