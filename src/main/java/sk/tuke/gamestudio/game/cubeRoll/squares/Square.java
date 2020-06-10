package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;

public interface Square {
    public int interactWith(Cube cube, String direction);
    public String printSquare();
    public String getImageName();
    public String getTooltipText();
}
