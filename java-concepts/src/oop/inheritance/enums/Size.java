package oop.inheritance.enums;

public enum Size {
    SMALL("S"), MEDIUM("M"), LARGE("l"), EXTRA_LARGE("XL");

    private final String abbreviation;

    private Size(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
