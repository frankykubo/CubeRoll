package sk.tuke.gamestudio.core;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import sk.tuke.gamestudio.game.cubeRoll.core.Level;
import sk.tuke.gamestudio.game.cubeRoll.core.TypeOfUI;
import sk.tuke.gamestudio.game.cubeRoll.squares.Square;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class LevelTest {
    private Level level;


    public LevelTest() {
    }

    @Test
    public void checkLevelWithNoMap(){
        try {
            level = new Level(null, 5, 7, TypeOfUI.CONSOLE, new Cube(0, 0, 5));
        } catch (Exception e) {
            System.out.println("Exception catched.");
        }
        assertNull(level);
    }

    @Test
    public void checkLevelWithNotSetRowsAndColumns(){
        try {
            level = new Level(new Square[0][0], 0, -2, TypeOfUI.CONSOLE, new Cube(0, 0, 5));
        } catch (Exception e) {
            System.out.println("Exception catched.");
        }
        assertNull(level);
    }

    @Test
    public void checkLevelWithNoCube(){
        try {
            level = new Level(new Square[0][0], 5, 7, TypeOfUI.CONSOLE, null);
        } catch (Exception e) {
            System.out.println("Exception catched.");
        }
        assertNull(level);
    }

    @Test
    public void checkLevelWithNoFinish(){
        try {
            level = new Level(new Square[5][5], 5, 7, TypeOfUI.CONSOLE, new Cube(0, 0, 5));
        } catch (Exception e) {
            System.out.println("Exception catched.");
        }
        assertNull(level);
    }



}
