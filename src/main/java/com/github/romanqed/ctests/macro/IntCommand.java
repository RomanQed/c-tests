package com.github.romanqed.ctests.macro;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NamedMacro("int")
public class IntCommand extends MacroCommand {

    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid int command arguments!");
        }
        int left = Integer.parseInt(arguments.get(0));
        int right = Integer.parseInt(arguments.get(1));
        return Integer.toString(ThreadLocalRandom.current().nextInt(left, right + 1));
    }
}
