package oop.inheritance.innerClasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;

public class AnonymousInnerClassTest {
    public static void main(String[] args) {
        var clock = new TalkingClockAnonymous();
        clock.start(1000, true);

        // keep program running until the user selects "OK"
        JOptionPane.showMessageDialog(null, "Quit program?");
        System.exit(0);
    }
}

class TalkingClockAnonymous {

    /**
     * Starts the clock.
     * @param interval the interval between messages (in milliseconds)
     * @param beep true if the clock should beep
     */
    public void start(int interval, boolean beep) {
        var listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("At the tone, the time is " + Instant.ofEpochMilli(actionEvent.getWhen()));
                if (beep) Toolkit.getDefaultToolkit().beep();
            }
        };

        var timer = new Timer(interval, listener);
        timer.start();
    }
}