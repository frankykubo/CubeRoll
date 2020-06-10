package sk.tuke.gamestudio.game.cubeRoll.core;

import sk.tuke.gamestudio.game.cubeRoll.squares.Big;
import sk.tuke.gamestudio.game.cubeRoll.squares.Moving;

public class Cube {
    private int positionX;
    private int positionY;
    private int[][] cubeSides;
    private int finishPos;
    private int[] temporarySaver;

    public Cube(int positionY, int positionX, int finishPos) {
        if(positionY< 0 || positionX < 0 || (finishPos < 1 || finishPos > 6))
            throw new IllegalArgumentException("Cube was not created because of wrong argument.");
        generateCube();
        this.positionX = positionX;
        this.positionY = positionY;
        this.finishPos = finishPos;
        temporarySaver = new int[4];
    }

    private void generateCube(){
        cubeSides = new int[3][4];
        cubeSides[0][2] = 1; //represented like [y][x]
        cubeSides[1][0] = 4;
        cubeSides[1][1] = 2;
        cubeSides[1][2] = 3;
        cubeSides[1][3] = 5;
        cubeSides[2][2] = 6;
    }

    public int getActualSide() {
        return cubeSides[1][2];
    }

    public int down(Level level){
        if(level == null) throw new IllegalArgumentException("Level in argument of method down is null!");
        positionY++;
        if(positionY >= level.getRows()) return 1;
        if(!(level.getSquare(positionX, positionY) instanceof Big) && level.getSquare(positionX, positionY-1) instanceof Big) rotateCubeDown();
        rotateCubeDown();
        if(checkIfSquareIsMoving(level) == 1) return 1;
        return level.getSquare(positionX, positionY).interactWith(this, "down");
    }

    public int up(Level level){
        if(level == null) throw new IllegalArgumentException("Level in argument of method down is null!");
        if(positionY == 0) return 1;
        positionY--;
        if(!(level.getSquare(positionX, positionY) instanceof Big) && level.getSquare(positionX, positionY+1) instanceof Big) rotateCubeUp(); //Dlaždica minulá
        rotateCubeUp();
        if(checkIfSquareIsMoving(level) == 1) return 1;
        return level.getSquare(positionX, positionY).interactWith(this, "up");
    }

    public int right(Level level){
        if(level == null) throw new IllegalArgumentException("Level in argument of method down is null!");
        positionX++;
        if(!(level.getSquare(positionX, positionY) instanceof Big) && level.getSquare(positionX-1, positionY) instanceof Big) rotateCubeRight();
        if(positionX >= level.getColumns()) return 1;
        rotateCubeRight();
        if(checkIfSquareIsMoving(level) == 1) return 1;
        return level.getSquare(positionX, positionY).interactWith(this, "right");
    }

    public int left(Level level){
        if(level == null) throw new IllegalArgumentException("Level in argument of method down is null!");
        if(positionX == 0) return 1;
        positionX--;
        if(!(level.getSquare(positionX, positionY) instanceof Big) && level.getSquare(positionX+1, positionY) instanceof Big) rotateCubeLeft();
        rotateCubeLeft();
        if(checkIfSquareIsMoving(level) == 1) return 1;
        return level.getSquare(positionX, positionY).interactWith(this, "left");
    }

    private int checkIfSquareIsMoving(Level level) {
        while (level.getSquare(positionX, positionY) instanceof Moving) {
                level.getSquare(positionX, positionY).interactWith(this, "");
                if(level.getSquare(positionX, positionY) == null) return 1;
        }
        return 0;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getFinishPos() {
        return finishPos;
    }

    public void restart(int x, int y){
        this.positionY = y;
        this.positionX = x;
        generateCube();
    }

    public int[][] getCubeSides() {
        return cubeSides;
    }

    public void setCubeSides(int x, int y, int value) {
        this.cubeSides[x][y] = value;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void rotateCubeDown(){
        temporarySaver[0] = cubeSides[0][2];
        temporarySaver[1] = cubeSides[1][0];
        temporarySaver[2] = cubeSides[1][2];
        temporarySaver[3] = cubeSides[2][2];
        cubeSides[0][2] = temporarySaver[2];
        cubeSides[1][0] = temporarySaver[0];
        cubeSides[1][2] = temporarySaver[3];
        cubeSides[2][2] = temporarySaver[1];
    }

    public void rotateCubeUp(){
        temporarySaver[0] = cubeSides[0][2];
        temporarySaver[1] = cubeSides[1][0];
        temporarySaver[2] = cubeSides[1][2];
        temporarySaver[3] = cubeSides[2][2];
        cubeSides[0][2] = temporarySaver[1];
        cubeSides[1][0] = temporarySaver[3];
        cubeSides[1][2] = temporarySaver[0];
        cubeSides[2][2] = temporarySaver[2];
    }

    public void rotateCubeLeft(){
        temporarySaver[0] = cubeSides[1][0];
        temporarySaver[1] = cubeSides[1][1];
        temporarySaver[2] = cubeSides[1][2];
        temporarySaver[3] = cubeSides[1][3];
        cubeSides[1][0] = temporarySaver[3];
        cubeSides[1][1] = temporarySaver[0];
        cubeSides[1][2] = temporarySaver[1];
        cubeSides[1][3] = temporarySaver[2];
    }

    public void rotateCubeRight(){
        temporarySaver[0] = cubeSides[1][0];
        temporarySaver[1] = cubeSides[1][1];
        temporarySaver[2] = cubeSides[1][2];
        temporarySaver[3] = cubeSides[1][3];
        cubeSides[1][0] = temporarySaver[1];
        cubeSides[1][1] = temporarySaver[2];
        cubeSides[1][2] = temporarySaver[3];
        cubeSides[1][3] = temporarySaver[0];
    }
}
