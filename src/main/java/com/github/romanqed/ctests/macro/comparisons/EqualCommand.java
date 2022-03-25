package com.github.romanqed.ctests.macro.comparisons;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("e")
public class EqualCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid equal command arguments!");
        }
        String left = arguments.get(0);
        String right = arguments.get(1);
        return Boolean.toString(left.equals(right));
    }
}
