package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Moving implements Square {
    private String squareDirection;

    public Moving(String direction) {
        if(!direction.equals("U") && !direction.equals("D") && !direction.equals("L") && !direction.equals("R")){
            throw new IllegalArgumentException("Square moving: BAD DIRECTION GIVEN TO CONSTRUCTOR!");
        }
        this.squareDirection = direction;
    }

    @Override
    public int interactWith(Cube cube, String direction) {
        switch (squareDirection) {
            case "U":
                cube.setPositionY(cube.getPositionY() - 1);

                break;
            case "D":
                cube.setPositionY(cube.getPositionY() + 1);
                break;
            case "L":
                cube.setPositionX(cube.getPositionX() - 1);
                break;
            case "R":
                cube.setPositionX(cube.getPositionX() + 1);
        }
        return 0;
    }

    @Override
    public String printSquare() {
        switch (squareDirection) {
            case "U":
                System.out.print(Color.BLACK);
                return Color.WHITE_BACKGROUND + " \u2191 " + Color.RESET;
            case "D":
                System.out.print(Color.BLACK);
                return Color.WHITE_BACKGROUND + " \u2193 " + Color.RESET;
            case "L":
                System.out.print(Color.BLACK);
                return Color.WHITE_BACKGROUND + " \u2190 " + Color.RESET;
            case "R":
                System.out.print(Color.BLACK);
                return Color.WHITE_BACKGROUND + " \u2192 " + Color.RESET;
        }
        return "BAD DIRECTION";
    }

    @Override
    public String getImageName() {
        switch (squareDirection) {
            case "U":
                return "movingUp.jpg";
            case "D":
                return "movingDown.jpg";
            case "L":
                return "movingLeft.jpg";
            case "R":
                return "movingRight.jpg";
        }
        return "BAD DIRECTION";
    }

    @Override
    public String getTooltipText() {
        return "A moving square. Basically moves you the way marked on it.";
    }
}
