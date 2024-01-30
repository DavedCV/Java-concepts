package game;

import game.GameRunner;
import game.MarioGame;
import game.SuperContraGame;

public class App01GamingBasicJava {
    public static void main(String[] args) {
        MarioGame marioGame = new MarioGame();
        SuperContraGame superContraGame = new SuperContraGame();

        // GameRunner gameRunner = new GameRunner(marioGame);
        GameRunner gameRunner = new GameRunner(superContraGame);

        gameRunner.run();
    }
}
