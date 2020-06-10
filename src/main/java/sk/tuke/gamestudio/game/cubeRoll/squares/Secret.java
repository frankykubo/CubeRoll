package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Secret implements Square {

    private boolean visible = false;

    @Override
    public int interactWith(Cube cube, String direction) {
        if(visible) {
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public String printSquare() {
        if(visible) {
            return Color.GREEN_BACKGROUND + "   " + Color.RESET;
        }else{
            return Color.BLACK_BACKGROUND + "   " + Color.RESET;
        }
    }

    @Override
    public String getImageName() {
        if(visible) {
            return "secret.jpg";
        }else{
            return "hole.jpg";
        }
    }

    @Override
    public String getTooltipText() {
        if(visible) {
            return "This is secret square that YOU have found, but it does nothing special.";
        }else{
            return "A hole. The level is restarted if you hop on this square and the number of deaths is increased.";
        }
    }

    public void append(){
        visible = true;
    }

    public void hide(){
        visible = false;
    }
}
