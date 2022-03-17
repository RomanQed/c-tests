package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.util.IOUtil;

public abstract class ConsoleCommand extends Command {
    protected String description;
    protected String help;

    public ConsoleCommand() {
        Class<?> clazz = getClass();
        NamedCommand command = clazz.getAnnotation(NamedCommand.class);
        if (command == null) {
            throw new IllegalStateException("Command annotate not found!");
        }
        this.name = command.value();
        Description description = clazz.getAnnotation(Description.class);
        if (description != null) {
            this.description = IOUtil.readResourceFile(description.value());
        }
        Help help = clazz.getAnnotation(Help.class);
        if (help != null) {
            this.help = IOUtil.readResourceFile(help.value());
        }
    }

    public String getDescription() {
        return description;
    }

    public String getHelp() {
        return help;
    }
}
