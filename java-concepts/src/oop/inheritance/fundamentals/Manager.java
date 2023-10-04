package oop.inheritance.fundamentals;

public class Manager extends Employee {

    private double bonus;

    public Manager(String name, Double salary, int year, int month, int day, double bonus) {
        super(name, salary, year, month, day);
        this.bonus = bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    // overriding methods
    @Override
    public double getSalary() {
        double baseSalary = super.getSalary();
        return baseSalary + bonus;
    }

    public static void main(String[] args) {
        Manager boss = new Manager("Harry", 5000.0, 2023, 6, 4, 500);

        var staff = new Employee[3];

        staff[0] = boss;
        staff[1] = new Employee("Juliet", 4000.0, 2023, 3, 1);
        staff[2] = new Employee("Joan", 3000.0, 2023, 3, 2);

        // polymorphism
        for (Employee e : staff) {
            System.out.println(e.getName() + " " + e.getSalary());
            if (e instanceof Manager) {
                System.out.println(e.getName() + " is instance of Manager and can be casted");
            }
        }
    }
}
