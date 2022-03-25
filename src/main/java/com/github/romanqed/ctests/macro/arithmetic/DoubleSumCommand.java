package com.github.romanqed.ctests.macro.arithmetic;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;

@NamedMacro("d_sum")
public class DoubleSumCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid sum command arguments!");
        }
        double left = Double.parseDouble(arguments.get(0));
        double right = Double.parseDouble(arguments.get(1));
        return Double.toString(left + right);
    }
}
