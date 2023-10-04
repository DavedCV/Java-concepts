package oop.inheritance.interfaces;

interface Person {
    default String getName() {
        return "";
    }
}

interface Named {
    default String getName() {
        return getClass().getName() + "_" + hashCode();
    }
}
public class DefaultConflicts implements Person, Named{
    public static void main(String[] args) {
        System.out.println(new DefaultConflicts().getName());
    }

    @Override
    public String getName() {
        return Named.super.getName();
    }
}
