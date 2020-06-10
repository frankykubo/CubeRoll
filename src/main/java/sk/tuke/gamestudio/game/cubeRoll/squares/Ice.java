package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Ice implements Square {

    private Square[][]map;

    public Ice(Square[][] map) {
        this.map = map;
    }

    @Override
    public int interactWith(Cube cube, String direction) {
        switch (direction) {
            case "down":
                while(map[cube.getPositionY()][cube.getPositionX()] instanceof Ice){
                    cube.setPositionY(cube.getPositionY()+1);
                }
                break;
            case "up":
                while(map[cube.getPositionY()][cube.getPositionX()] instanceof Ice){
                    cube.setPositionY(cube.getPositionY()-1);
                }
                break;
            case "left":
                while(map[cube.getPositionY()][cube.getPositionX()] instanceof Ice){
                    cube.setPositionX(cube.getPositionX()-1);
                }
                break;
            case "right":
                while(map[cube.getPositionY()][cube.getPositionX()] instanceof Ice){
                    cube.setPositionX(cube.getPositionX()+1);
                }
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public String printSquare() {
        System.out.print(Color.BLACK);
        return Color.WHITE_BACKGROUND + " \u2195 " + Color.RESET;
    }

    @Override
    public String getImageName() {
        return "ice.jpg";
    }

    @Override
    public String getTooltipText() {
        return "An ice. You will continue to move on the way you hopped on this square until some other type of the square is reached.";
    }
}
