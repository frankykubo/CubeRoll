package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Normal implements Square {
    @Override
    public int interactWith(Cube cube, String direction) {
        return 0;
    }
    @Override
    public String printSquare() {
        return Color.WHITE_BACKGROUND + "   " + Color.RESET;
    }

    @Override
    public String getImageName() {
        return "normal.jpg";
    }

    @Override
    public String getTooltipText() {
        return "A normal square, you can use it how many times you want and without any limitations.";
    }
}
