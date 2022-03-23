package com.github.romanqed.ctests.macro;

import java.util.List;

@NamedMacro("d_div")
public class DoubleDivideCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid div command arguments!");
        }
        double left = Double.parseDouble(arguments.get(0));
        double right = Double.parseDouble(arguments.get(1));
        return Double.toString(left / right);
    }
}
