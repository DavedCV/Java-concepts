package com.davidcv.spring28minuteslearn.game;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@Qualifier("superMarioGameQualifier")
public class MarioGame implements GameConsole{

    public void up() {
        System.out.println("Jump (Mario)");
    }

    public void left() {
        System.out.println("Go back (Mario)");
    }

    public void right() {
        System.out.println("Accelerate (Mario)");
    }

    public void down() {
        System.out.println("Go into a hole (Mario)");
    }
}
