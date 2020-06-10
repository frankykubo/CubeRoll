package sk.tuke.gamestudio.game;

import sk.tuke.gamestudio.game.cubeRoll.core.Game;
import sk.tuke.gamestudio.game.cubeRoll.core.TypeOfUI;

public class CubeRoll {
    public static void main(String[] args) {
        Game game = new Game(TypeOfUI.CONSOLE);
        int state  = game.playGame();
        if(state == 0){
            System.out.println("Thanks for playing. See you soon!");
        }else{
            System.out.println("Error occurred somewhere!");
        }
    }
}
