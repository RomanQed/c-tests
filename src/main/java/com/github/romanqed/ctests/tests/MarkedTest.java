package com.github.romanqed.ctests.tests;

import com.github.romanqed.jutils.util.Checks;

import java.util.Objects;

public class MarkedTest extends Test {
    private TestType type;
    private int number;

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = Objects.requireNonNull(type);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        Checks.requireCorrectValue(number, e -> e > 0);
        this.number = number;
    }

    @Override
    public String toString() {
        return "Number: " + number + " Type: " + type + " " + super.toString();
    }
}
