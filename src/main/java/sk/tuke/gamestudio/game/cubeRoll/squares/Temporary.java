package sk.tuke.gamestudio.game.cubeRoll.squares;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.ui.Color;

public class Temporary implements Square {
    private int remainingUses;
    private int capacityOfUses;

    public Temporary(int remainingUses) {
        this.remainingUses = remainingUses;
        this.capacityOfUses = remainingUses;
    }

    @Override
    public int interactWith(Cube cube, String direction) {
        if(remainingUses < 1){
            return 1;
        }else{
            remainingUses--;
        }
        return 0;
    }

    @Override
    public String printSquare() {
        if(remainingUses > 0) {
            System.out.print(Color.BLACK);
            return Color.WHITE_BACKGROUND + " " + remainingUses + " " + Color.RESET;
        }else{
            return Color.BLACK_BACKGROUND + "   " + Color.RESET;
        }
    }

    @Override
    public String getImageName() {
        if(remainingUses > 0) {
            return "temporary"+remainingUses+".jpg";
        }else{
            return "hole.jpg";
        }
    }

    @Override
    public String getTooltipText() {
        if(remainingUses > 0) {
            return "A temporary square. The number " + remainingUses + " specifies, how many times you can use this square.";
        }else{
            return "A hole. The level is restarted if you hop on this square and the number of deaths is increased.";
        }
    }

    public void reset(){
        remainingUses = capacityOfUses;
    }
}
