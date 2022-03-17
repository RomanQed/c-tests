package com.github.romanqed.ctests.commands;

import java.util.Objects;

public abstract class ConsoleCommand extends Command {
    protected String help;

    public ConsoleCommand() {
        Class<?> clazz = getClass();
        NamedCommand command = clazz.getAnnotation(NamedCommand.class);
        if (command == null) {
            throw new IllegalStateException("Command annotate not found!");
        }
        this.name = command.value();
        Help help = clazz.getAnnotation(Help.class);
        if (help != null) {
            this.help = help.value();
        }
    }

    public ConsoleCommand(String name, String description, String help) {
        this.name = Objects.requireNonNull(name);
        this.help = Objects.requireNonNull(help);
    }

    public ConsoleCommand(String name, String description) {
        this(name, description, "");
    }

    public ConsoleCommand(String name) {
        this(name, "", "");
    }

    public String getHelp() {
        return help;
    }
}
