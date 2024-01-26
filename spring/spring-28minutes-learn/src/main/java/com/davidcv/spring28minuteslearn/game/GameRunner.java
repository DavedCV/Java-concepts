package com.davidcv.spring28minuteslearn.game;

public class GameRunner {

    private GameConsole game;

    public GameRunner(GameConsole game) {
        this.game = game;
    }

    public void run() {
        System.out.println("Running game: " + game);
        game.up();
        game.left();
        game.right();
        game.down();
    }
}
