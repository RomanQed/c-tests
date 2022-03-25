package com.github.romanqed.ctests.macro.logic;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("if")
public class IfCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() < 2) {
            throw new IllegalArgumentException("Invalid if command arguments!");
        }
        boolean condition = Boolean.parseBoolean(arguments.get(0));
        String thenRet = arguments.get(1);
        String elseRet = "";
        if (arguments.size() == 3) {
            elseRet = arguments.get(2);
        }
        return condition ? thenRet : elseRet;
    }
}
