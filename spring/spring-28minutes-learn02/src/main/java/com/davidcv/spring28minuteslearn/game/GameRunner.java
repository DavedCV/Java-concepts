package com.davidcv.spring28minuteslearn.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class GameRunner {

    // @Autowired
    private GameConsole game;

    @Autowired
    public GameRunner(@Qualifier("superContraGameQualifier") GameConsole game) {
        this.game = game;
    }

    // @Autowired
    public void setGame(GameConsole game) {
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
