package com.github.romanqed.ctests.macro;

import java.util.List;

@NamedMacro("i_mul")
public class IntMultiplyCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid mul command arguments!");
        }
        int left = Integer.parseInt(arguments.get(0));
        int right = Integer.parseInt(arguments.get(1));
        return Integer.toString(left * right);
    }
}
