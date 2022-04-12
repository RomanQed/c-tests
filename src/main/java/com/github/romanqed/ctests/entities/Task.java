package com.github.romanqed.ctests.entities;

import com.github.romanqed.ctests.base.AbstractEntity;
import com.github.romanqed.jutils.util.Checks;

import java.util.UUID;

public class Task extends AbstractEntity {
    private int lab;
    private int number;

    public Task(UUID uuid) {
        super(uuid);
    }

    public Task() {
        super(null);
    }

    public int getLab() {
        return lab;
    }

    public void setLab(int lab) {
        this.lab = Checks.requireCorrectValue(lab, e -> e > 0);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = Checks.requireCorrectValue(number, e -> e > 0);
    }
}
