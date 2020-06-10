package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Finish implements Square {

    @Override
    public int interactWith(Cube cube, String direction) {
        return 0;
    }

    @Override
    public String printSquare() {
        System.out.print(Color.BLACK);
        return Color.YELLOW_BACKGROUND + " F " + Color.RESET;
    }

    @Override
    public String getImageName() {
        return "finish.jpg";
    }

    @Override
    public String getTooltipText() {
        return "A finish. The color of down side of cube has to be yellow if you want to end the level!";
    }
}
