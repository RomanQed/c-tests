package com.github.romanqed.ctests.macro;

import java.util.List;
import java.util.Objects;

public class ArrayCommand extends MacroCommand {
    private final MacroCommand command;

    protected ArrayCommand(MacroCommand command) {
        this.command = Objects.requireNonNull(command);
    }

    @Override
    public String execute(List<String> arguments) throws Exception {
        if (arguments.size() != 3) {
            throw new IllegalArgumentException("Invalid array command arguments!");
        }
        int length = Integer.parseInt(arguments.get(0));
        StringBuilder builder = new StringBuilder();
        List<String> commandArguments = arguments.subList(1, 3);
        for (int i = 0; i < length; ++i) {
            builder.append(command.execute(commandArguments)).append(' ');
        }
        String ret = builder.toString();
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }
}
