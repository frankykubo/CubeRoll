package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Hole implements Square {
    @Override
    public int interactWith(Cube cube, String direction) {
        return 1;
    }

    @Override
    public String printSquare() {
        return Color.BLACK_BACKGROUND + "   " + Color.RESET;
    }

    @Override
    public String getImageName() {
        return "hole.jpg";
    }

    @Override
    public String getTooltipText() {
        return "A hole. The level is restarted if you hop on this square and the number of deaths is increased.";
    }

}
