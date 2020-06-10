package sk.tuke.gamestudio.game.cubeRoll.core;

import sk.tuke.gamestudio.game.cubeRoll.squares.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LevelFactory {

    private Square[][] finalMap;

    public LevelFactory() {
    }

    public Level createLevel(int number, TypeOfUI typeOfUI){
        Cube cube;
        File file = new File("src\\map");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        while(sc.hasNextLine() && !(line = sc.nextLine()).equals(String.format("%s%s", "level", number))){
            if(!sc.hasNextLine())
                throw new IllegalArgumentException("Error while creating level " + number + ": this level does not exist.");
        }
        line = sc.nextLine();
        int height = Integer.parseInt(String.valueOf(line.charAt(0)));
        int width  = Integer.parseInt(String.valueOf(line.charAt(1)));
        finalMap = generateMap(height, width);
        while (sc.hasNextLine() && !(line = sc.nextLine()).matches("^cube")) {
            int y = Integer.parseInt(String.valueOf(line.charAt(0)));
            int x = Integer.parseInt(String.valueOf(line.charAt(1)));
            switch (line.charAt(2)){
                case 'B':
                    finalMap[y][x] = new Big(finalMap);
                    break;
                case 'C':
                    finalMap[y][x] = new Colored(Character.getNumericValue(line.charAt(3)));
                    break;
                case 'F':
                    finalMap[y][x] = new Finish();
                    break;
                case 'H':
                    finalMap[y][x] = new Hole();
                    break;
                case 'I':
                    finalMap[y][x] = new Ice(finalMap);
                    break;
                case 'M':
                    finalMap[y][x] = new Moving(Character.toString(line.charAt(3)));
                    break;
                case 'N':
                    finalMap[y][x] = new Normal();
                    break;
                case 'P':
                    finalMap[y][x] = new PushButton((Secret) finalMap[Integer.parseInt(String.valueOf(line.charAt(3)))][Integer.parseInt(String.valueOf(line.charAt(4)))]);
                    break;
                case 'S':
                    finalMap[y][x] = new Secret();
                    break;
                case 'T':
                    finalMap[y][x] = new Temporary(Character.getNumericValue(line.charAt(3)));
                    break;
            }
        }
        line = sc.nextLine();
        cube = new Cube(Integer.parseInt(String.valueOf(line.charAt(0))), Integer.parseInt(String.valueOf(line.charAt(1))), Integer.parseInt(String.valueOf(line.charAt(2))));
        return new Level(finalMap, height, width, typeOfUI, cube);
    }

    private Square[][] generateMap(int height, int width){
        return new Square[height][width];
    }
}
