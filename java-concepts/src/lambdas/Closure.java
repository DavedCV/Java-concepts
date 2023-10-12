package lambdas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Closure {
    public static void repeatMessage(String text, int delay) {
        ActionListener listener = event -> {
            System.out.println(text);
            Toolkit.getDefaultToolkit().beep();
        };

        new Timer(delay, listener).start();
    }

    public static void main(String[] args) {
        repeatMessage("Hello", 500);
        while (true) {}
    }
}
