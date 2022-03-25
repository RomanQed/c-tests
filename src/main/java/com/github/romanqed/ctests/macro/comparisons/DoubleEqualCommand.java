package com.github.romanqed.ctests.macro.comparisons;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("de")
public class DoubleEqualCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid equal command arguments!");
        }
        double left = Double.parseDouble(arguments.get(0));
        double right = Double.parseDouble(arguments.get(1));
        return Boolean.toString(Double.compare(left, right) == 0);
    }
}
