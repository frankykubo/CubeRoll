package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Big implements Square {

    private Square[][]map;

    public Big(Square[][] map) {
        this.map = map;
    }

    @Override
    public int interactWith(Cube cube, String direction) {
        int[] temporarySaver = new int[4];
        switch (direction) {
            case "down":
                if(map[cube.getPositionY()-1][cube.getPositionX()] instanceof Big) return 0;
                cube.rotateCubeDown();
                break;
            case "up":
                if(map[cube.getPositionY()+1][cube.getPositionX()] instanceof Big) return 0;
                cube.rotateCubeUp();
                break;
            case "left":
                if(map[cube.getPositionY()][cube.getPositionX()+1] instanceof Big) return 0;
                cube.rotateCubeLeft();
                break;
            case "right":
                if(map[cube.getPositionY()][cube.getPositionX()-1] instanceof Big) return 0;
                cube.rotateCubeRight();
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public String printSquare() {
        System.out.print(Color.BLACK);
        return Color.WHITE_BACKGROUND + " T " + Color.RESET;
    }

    @Override
    public String getImageName() {
        return "big.jpg";
    }

    @Override
    public String getTooltipText() {
        return "High ground. This square is one level higher than all the others, so you will rotate twice.";
    }

    public void setMap(Square[][] map) {
        this.map = map;
    }
}
