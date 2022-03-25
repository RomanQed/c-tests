package com.github.romanqed.ctests.macro.arithmetic;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("i_sum")
public class IntSumCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid sum command arguments!");
        }
        int left = Integer.parseInt(arguments.get(0));
        int right = Integer.parseInt(arguments.get(1));
        return Integer.toString(left + right);
    }
}
