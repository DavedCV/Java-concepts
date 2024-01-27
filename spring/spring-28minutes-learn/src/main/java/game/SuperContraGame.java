package game;

public class SuperContraGame implements GameConsole{
    public void up() {
        System.out.println("Jump (Contra)");
    }

    public void left() {
        System.out.println("Go back (Contra)");
    }

    public void right() {
        System.out.println("Shoot a bullet (Contra)");
    }

    public void down() {
        System.out.println("Sit down (Contra)");
    }
}
