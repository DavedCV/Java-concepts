package generics;

import java.util.Objects;

public class Manager extends Employee {

    private double bonus;

    public Manager(String name, Double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        this.bonus = 0;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getBonus() {
        return bonus;
    }

    @Override
    public double getSalary() {
        double baseSalary = super.getSalary();
        return baseSalary + bonus;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public boolean equals(Object otherObject) {

        // when the superclass compare by the getClass, call the super equals and continue with field validations
        if (!super.equals(otherObject)) return false;

        // super.equals checked that this and other belong to the same class
        var other = (Manager) otherObject;

        return bonus == other.bonus;
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), bonus);
    }

    public String toString() {
        return super.toString() + "[bonus=" + bonus + "]";
    }
}
