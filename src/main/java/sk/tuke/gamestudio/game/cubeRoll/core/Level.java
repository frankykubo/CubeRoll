package sk.tuke.gamestudio.game.cubeRoll.core;

import sk.tuke.gamestudio.game.cubeRoll.ui.Color;
import sk.tuke.gamestudio.game.cubeRoll.ui.ConsoleUI;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.cubeRoll.squares.*;
import sk.tuke.gamestudio.server.controller.CubeRollController;

public class Level {
    private Square[][] map;
    private int rows;
    private int columns;
    private TypeOfUI typeOfUI;
    private Cube cube;
    private int state;
    private Score score;
    private int startX;
    private int startY;

    public Level(Square[][] map, int rows, int columns, TypeOfUI typeOfUI, Cube cube) {
        if(map == null | rows < 1 | columns < 1 | typeOfUI == null | cube == null)
            throw new IllegalArgumentException("Level was not created because of wrong argument.");
        boolean containsFinish = false;
        for (Square[] a : map) {
            for (Square b : a) {
                if(b instanceof Finish) containsFinish = true;
            }
        }
        if(!containsFinish) throw new IllegalArgumentException("Level was not created because of map that has no finish.");
        this.rows = rows;
        this.columns = columns;
        this.map = map;
        this.typeOfUI = typeOfUI;
        this.cube = cube;
        this.startX = cube.getPositionX();
        this.startY = cube.getPositionY();
        this.state = 0;
        if(typeOfUI == TypeOfUI.GRAPHICAL){
            score = CubeRollController.getScore();
        }else{
            score = Game.getScore();
        }
    }

    public boolean isFinished(){
        return map[cube.getPositionY()][cube.getPositionX()] instanceof Finish && cube.getActualSide() == cube.getFinishPos();
    }

    public int play(String direction){
        if(typeOfUI == TypeOfUI.CONSOLE){
            ConsoleUI consoleUI = ConsoleUI.getInstance();
            consoleUI.setLevel(this);
            while (state == 0 && !isFinished()) {
                consoleUI.printStep();
                consoleUI.printCube();
                switch (consoleUI.getInput()) {
                    case "UP":
                        state = cube.up(this);
                        score.increaseSteps();
                        continue;
                    case "DOWN":
                        state = cube.down(this);
                        score.increaseSteps();
                        continue;
                    case "RIGHT":
                        state = cube.right(this);
                        score.increaseSteps();
                        continue;
                    case "LEFT":
                        state = cube.left(this);
                        score.increaseSteps();
                        continue;
                    case "COMMANDS":
                        consoleUI.printCommands();
                        System.out.println(Color.YELLOW_BOLD +"When you are ready, type \"yes\"." + Color.RESET);
                        while(!consoleUI.getInput().equals("YES")){
                            consoleUI.handleReturnValue(5);
                        }
                        continue;
                    case "LEGEND":
                        consoleUI.printLegend();
                        System.out.println(Color.YELLOW_BOLD + "When you are ready, type \"yes\"." + Color.RESET);
                        while(!consoleUI.getInput().equals("YES")){
                            consoleUI.handleReturnValue(5);
                            System.out.println(Color.YELLOW_BOLD + "When you are ready, type \"yes\"." + Color.RESET);
                        }
                        continue;
                    case "RESET":
                        return 3;
                    case "EXIT":
                        return 2;
                    default:
                        consoleUI.handleReturnValue(5);
                }
            }
            if(state != 1)
            if(isFinished()){
                consoleUI.printStep();
                return 0;
            }
            return state;
        }else{
                switch (direction) {
                    case "UP":
                        state = cube.up(this);
                        score.increaseSteps();
                        break;
                    case "DOWN":
                        state = cube.down(this);
                        score.increaseSteps();
                        break;
                    case "RIGHT":
                        state = cube.right(this);
                        score.increaseSteps();
                        break;
                    case "LEFT":
                        state = cube.left(this);
                        score.increaseSteps();
                        break;
                    case "COMMANDS":
                        break;
                    case "LEGEND":
                        break;
                    case "RESET":
                        return 3;
                    case "EXIT":
                        return 2;
                    default:
                        return -1;
                }
            if(state != 1)
                if(isFinished()){
                    return 5;
                }
            return state;
        }
    }

    public Square[][] getMap() {
        return map;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Cube getCube() {
        return cube;
    }

    public Square getSquare(int x, int y){
        if(x >= columns || x < 0 || y >= rows || y < 0){
            return null;
        }else
            return map[y][x];
    }

    public Score getScore() {
        return score;
    }

    public void fallToTheHole(){
        this.cube.restart(startX, startY);
        state = 0;
        score.increaseDeaths();
        score.resetSteps();
        resetInteractiveSquares();
    }

    public void resetLevelByPlayer(){
        this.cube.restart(startX, startY);
        state = 0;
        score.resetSteps();
        resetInteractiveSquares();
    }

    private void resetInteractiveSquares(){
        for(int i = 0; i < rows; i++){
            for(int q = 0; q < columns; q++){
                if(getSquare(q, i) instanceof Temporary){
                    ((Temporary) getSquare(q, i)).reset();
                }else if(getSquare(q, i) instanceof Secret){
                    ((Secret) getSquare(q, i)).hide();
                }
                else if(getSquare(q, i) instanceof PushButton){
                    ((PushButton) getSquare(q, i)).resetPushButton();
                }

            }
        }
    }
}
