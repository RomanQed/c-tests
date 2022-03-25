package com.github.romanqed.ctests.macro.logic;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("not")
public class NotCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) throws Exception {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("Invalid not command arguments!");
        }
        boolean value = Boolean.parseBoolean(arguments.get(0));
        return Boolean.toString(!value);
    }
}
