package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class PushButton implements Square {

    private Secret secret;
    private boolean pushed = false;

    public PushButton(Secret secret) {
        this.secret = secret;
    }

    @Override
    public int interactWith(Cube cube, String direction) {
        secret.append();
        pushed = true;
        return 0;
    }
    @Override
    public String printSquare() {
        if(!pushed) {
            System.out.print(Color.BLACK);
            return Color.GREEN_BACKGROUND + " P " + Color.RESET;
        }else{
            return Color.GREEN_BACKGROUND + "   " + Color.RESET;
        }
    }

    @Override
    public String getImageName() {
        return "pushButton.jpg";
    }

    @Override
    public String getTooltipText() {
        return "A push button. When you hop on this square, there will appear the hidden square on the map.";
    }

    public void resetPushButton(){
        pushed = false;
    }
}
