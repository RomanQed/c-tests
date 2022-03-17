package com.github.romanqed.ctests.commands;

import com.github.romanqed.jutils.util.Checks;
import com.github.romanqed.jutils.util.Handler;

import java.util.List;

public abstract class Command implements Handler<List<String>> {
    public static final String DEFAULT_COMMAND_NAME = "command";

    protected String name;

    public Command(String name) {
        this.name = Checks.requireNonNullElse(name, DEFAULT_COMMAND_NAME);
    }

    public Command() {
        this(DEFAULT_COMMAND_NAME);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Command)) {
            return false;
        }
        return name.hashCode() == obj.hashCode();
    }

    @Override
    public String toString() {
        return "Command " + name;
    }
}
