package com.github.romanqed.ctests.macro.logic;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("or")
public class OrCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid or command arguments!");
        }
        boolean left = Boolean.parseBoolean(arguments.get(0));
        boolean right = Boolean.parseBoolean(arguments.get(1));
        return Boolean.toString(left || right);
    }
}
