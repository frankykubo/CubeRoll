package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Colored implements Square {
    private int TYPE; //1 = Finish side can stay on this platform, 2 = Every other side can stay at this platform
    public Colored(int type) {
        if(type != 1 && type != 2)
            throw new IllegalArgumentException("BAD TYPE FOR COLORED SQUARE.");
        this.TYPE = type;
    }

    @Override
    public int interactWith(Cube cube, String direction) {
        switch (TYPE){
            case 1:
                if(cube.getActualSide() == cube.getFinishPos()) return 0;
                break;
            case 2:
                if(cube.getActualSide() != cube.getFinishPos()) return 0;
                break;
        }
        return 1;
    }
    @Override
    public String printSquare() {
        switch (TYPE){
            case 1:
                return Color.YELLOW_BACKGROUND + "   " + Color.RESET;
            case 2:
                return Color.BLUE_BACKGROUND + "   " + Color.RESET;
        }
        return "Something is wrong here";
    }

    @Override
    public String getImageName() {
        switch (TYPE){
            case 1:
                return "coloredYellow.jpg";
            case 2:
                return "coloredBlue.jpg";
            default:
                return "Failed to load image name.";
        }
    }

    @Override
    public String getTooltipText() {
        return "The classic square, but the color of the down side of cube has to match the color of this square.";
    }
}
