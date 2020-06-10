package sk.tuke.gamestudio.core;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.cubeRoll.core.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    public GameTest() {
    }

    @Test
    public void createGameWithoutDefiningUI(){
        try {
            game = new Game(null);
        } catch (Exception e) {
            System.out.println("Exception catched.");
        }
        assertNull(game);
    }

    @Test
    public void gameStatesTest(){
        Game game = new Game(TypeOfUI.CONSOLE);
        LevelFactory factory = new LevelFactory();
        Level level = factory.createLevel(1, TypeOfUI.CONSOLE);
        Cube cube = level.getCube();
        int state;
        state = cube.down(level);
        assertEquals(0, state);
        state = cube.right(level);
        assertEquals(0, state);
        state = cube.down(level);
        assertEquals(1, state);
    }

    @Test
    public void finishGameTest(){
        Game game = new Game(TypeOfUI.CONSOLE);
        LevelFactory factory = new LevelFactory();
        Level level = factory.createLevel(1, TypeOfUI.CONSOLE);
        Cube cube = level.getCube();
        cube.down(level);
        cube.right(level);
        cube.right(level);
        cube.right(level);
        cube.right(level);
        cube.right(level);
        assertTrue(level.isFinished());
    }
}
