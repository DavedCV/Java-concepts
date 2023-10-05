package oop.inheritance.lambdas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.time.Instant;

public class MethodReference {
    public static void main(String[] args) {
        // Create an instance of RepeatedGreeter
        RepeatedGreeter greeter = new RepeatedGreeter();
        // Simulate an ActionEvent to trigger the greet method
        ActionEvent event = new ActionEvent(greeter, ActionEvent.ACTION_PERFORMED, "Simulated event");
        greeter.greet(event);
        while (true){}
    }
}

class Greeter {
    public void greet(ActionEvent event) {
        System.out.println("Hello, the times is: " + Instant.ofEpochMilli(event.getWhen()));
    }
}

class RepeatedGreeter extends Greeter {
    public void greet(ActionEvent event) {
        // using the superclass version of the method using a method reference
        var timer = new Timer(1000, super::greet);
        timer.start();
    }
}
