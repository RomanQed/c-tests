package com.github.romanqed.ctests.entities;

import com.github.romanqed.ctests.base.AbstractEntity;
import com.github.romanqed.jutils.util.Checks;

import java.util.UUID;

public class Lab extends AbstractEntity {
    private int number;

    public Lab(UUID uuid) {
        super(uuid);
    }

    public Lab() {
        super(null);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = Checks.requireCorrectValue(number, e -> e > 0);
    }
}
