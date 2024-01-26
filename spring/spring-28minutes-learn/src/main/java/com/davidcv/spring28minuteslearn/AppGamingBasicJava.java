package com.davidcv.spring28minuteslearn;

import com.davidcv.spring28minuteslearn.game.GameRunner;
import com.davidcv.spring28minuteslearn.game.MarioGame;
import com.davidcv.spring28minuteslearn.game.SuperContraGame;

public class AppGamingBasicJava {
    public static void main(String[] args) {
        MarioGame marioGame = new MarioGame();
        SuperContraGame superContraGame = new SuperContraGame();

        // GameRunner gameRunner = new GameRunner(marioGame);
        GameRunner gameRunner = new GameRunner(superContraGame);

        gameRunner.run();
    }
}
