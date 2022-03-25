package com.github.romanqed.ctests.macro.comparisons;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("ig")
public class IntGreaterCommand extends MacroCommand {

    @Override
    public String execute(List<String> arguments) throws Exception {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid greater command arguments!");
        }
        int left = Integer.parseInt(arguments.get(0));
        int right = Integer.parseInt(arguments.get(1));
        return Boolean.toString(left > right);
    }
}
