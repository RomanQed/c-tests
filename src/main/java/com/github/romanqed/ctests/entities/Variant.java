package com.github.romanqed.ctests.entities;

import com.github.romanqed.ctests.base.AbstractEntity;
import com.github.romanqed.jutils.util.Checks;

import java.util.UUID;

public class Variant extends AbstractEntity {
    private int lab;
    private int task;
    private int number;

    public Variant(UUID uuid) {
        super(uuid);
    }

    public Variant() {
        super(null);
    }

    public int getLab() {
        return lab;
    }

    public void setLab(int lab) {
        this.lab = Checks.requireCorrectValue(lab, e -> e > 0);
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = Checks.requireCorrectValue(task, e -> e > 0);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
