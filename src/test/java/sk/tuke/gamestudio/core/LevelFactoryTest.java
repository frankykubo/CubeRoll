package sk.tuke.gamestudio.core;

import sk.tuke.gamestudio.game.cubeRoll.core.LevelFactory;
import sk.tuke.gamestudio.game.cubeRoll.core.TypeOfUI;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelFactoryTest {

    private LevelFactory factory;

    public LevelFactoryTest() {
        this.factory = new LevelFactory();
    }

    @Test
    public void sendWrongArgumentToCreateLevelMethod(){
        boolean exceptionCatched = false;
        try{
            factory.createLevel(-7, TypeOfUI.CONSOLE);
        }catch (Exception e){
            exceptionCatched = true;
        }
        assertTrue(exceptionCatched);
    }
}
