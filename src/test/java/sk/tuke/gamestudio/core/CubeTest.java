package sk.tuke.gamestudio.core;

import sk.tuke.gamestudio.game.cubeRoll.core.Cube;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CubeTest {
    private Cube cube;

    public CubeTest() {
    }

    @Test
    public void checkCubeWithWrongXCoordinate(){
        try {
            cube = new Cube(1, -8, 5);
        }catch (Exception e){
            System.out.println("Exception catched.");
        }
        assertNull(cube);
    }
    @Test
    public void checkCubeWithWrongYCoordinate(){
        try {
            cube = new Cube(-2, 1, 5);
        }catch (Exception e){
            System.out.println("Exception catched.");
        }
        assertNull(cube);
    }
    @Test
    public void checkCubeWithWrongFinishPosition(){
        try {
            cube = new Cube(1, 1, 9);
        }catch (Exception e){
            System.out.println("Exception catched.");
        }
        assertNull(cube);
    }

    @Test
    public void testDirectionsWithNoLevelInParameter(){
        cube = new Cube(1, 1, 6);
        boolean assertVariable = true;
        try{
            cube.down(null);
        }catch (Exception e){
            System.out.println("Exception catched.");
            assertVariable = false;
        }
        assertFalse(assertVariable);
        assertVariable = true;
        try{
            cube.up(null);
        }catch (Exception e){
            System.out.println("Exception catched.");
            assertVariable = false;
        }
        assertFalse(assertVariable);
        assertVariable = true;
        try{
            cube.right(null);
        }catch (Exception e){
            System.out.println("Exception catched.");
            assertVariable = false;
        }
        assertFalse(assertVariable);
        assertVariable = true;
        try{
            cube.left(null);
        }catch (Exception e){
            System.out.println("Exception catched.");
            assertVariable = false;
        }
        assertFalse(assertVariable);
    }

}
