package com.github.romanqed.ctests.macro.comparisons;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("dg")
public class DoubleGreaterCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) throws Exception {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid greater command arguments!");
        }
        double left = Double.parseDouble(arguments.get(0));
        double right = Double.parseDouble(arguments.get(1));
        return Boolean.toString(left > right);
    }
}
