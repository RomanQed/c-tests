package com.github.romanqed.ctests.tasks;

import com.github.romanqed.jutils.util.Checks;

public class TaskData {
    protected int labNumber;
    protected int number;
    protected int variant;

    public TaskData() {
        this.labNumber = 1;
        this.number = 1;
        this.variant = 0;
    }

    public int getLabNumber() {
        return labNumber;
    }

    public void setLabNumber(int labNumber) {
        Checks.requireCorrectValue(number, e -> e > 0);
        this.labNumber = labNumber;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        Checks.requireCorrectValue(number, e -> e > 0);
        this.number = number;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        return "LabNumber: " + labNumber + " Number: " + number + " Variant: " + variant;
    }
}
