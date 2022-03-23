package com.github.romanqed.ctests.macro;

import com.github.romanqed.jutils.util.Action;

import java.util.List;

public abstract class MacroCommand implements Action<List<String>, String> {
    private final String name;

    public MacroCommand() {
        NamedMacro annotation = getClass().getAnnotation(NamedMacro.class);
        if (annotation == null) {
            throw new IllegalStateException("Can't find command annotation!");
        }
        this.name = annotation.value();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Command: " + name;
    }
}
