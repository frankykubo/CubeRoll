package sk.tuke.gamestudio.game.cubeRoll.core;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.cubeRoll.ui.ConsoleUI;
import sk.tuke.gamestudio.entity.Score;

public class Game {
    private LevelFactory levelFactory;
    private static int level;
    private TypeOfUI typeOfUI;
    private static Score score;
    private static final int MAX_LVL = 10;
    @Autowired
    private Menu menu;

    public Game(TypeOfUI typeOfUI) {
        if(typeOfUI == null) throw new IllegalArgumentException("You must define TypeOfUI if you want to create a game!");
        this.typeOfUI = typeOfUI;
        levelFactory = new LevelFactory();
        score = new Score();
    }

    public int playGame() {
        level = 1;
        score.resetAll();
        boolean exit = false;
        if (typeOfUI == TypeOfUI.CONSOLE) {
            while (menu.mainMenu(TypeOfUI.CONSOLE) == 0) {
                level = 3;
                score.resetAll();
                exit = false;
                ConsoleUI ui = ConsoleUI.getInstance();
                while (level <= MAX_LVL && !exit) {
                    Level actualLevel = levelFactory.createLevel(level, typeOfUI);
                    int state;
                    while (!exit && (state = actualLevel.play("No direction")) != 0) {
                        switch (state) {
                            case -1:
                                throw new RuntimeException("Niekde sa stala chyba");
                            case 1:
                                ui.handleReturnValue(1);
                                actualLevel.fallToTheHole();
                                if(level == 1) score.resetSteps();
                                break;
                            case 2:
                                exit = true;
                                break;
                            case 3:
                                actualLevel.resetLevelByPlayer();
                        }
                    }
                    score.updatePoints();
                    if (level == MAX_LVL) {
                        if (!exit) System.out.println("CONGRATULATIONS, YOU NAILED LAST LEVEL!");
                        exit = true;
                    }
                    if (level + 1 <= MAX_LVL && !exit) {
                        ui.handleReturnValue(0);
                        level++;
                        score.resetSteps();
                        score.resetDeaths();
                    } else {
                        if (level >= 2 && score.getPoints() > 1) {
                            if (level == MAX_LVL) {
                                System.out.println("Your score is " + score.getPoints());
                                menu.addScore(TypeOfUI.CONSOLE);
                            }
                            menu.addComment(TypeOfUI.CONSOLE);
                            menu.addRating(TypeOfUI.CONSOLE);
                        }
                    }
                }
            }
        } else {
            return -1; //Graphicke UI nepouziva tuto triedu
        }
        return -1;
    }

    public static Score getScore() {
        return score;
    }

    public static int getLevel() {
        return level;
    }
}
