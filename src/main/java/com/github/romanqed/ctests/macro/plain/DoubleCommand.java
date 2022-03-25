package com.github.romanqed.ctests.macro.plain;

import com.github.romanqed.ctests.macro.Array;
import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NamedMacro("double")
@Array("double")
public class DoubleCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid double command arguments!");
        }
        double left = Double.parseDouble(arguments.get(0));
        double right = Double.parseDouble(arguments.get(1));
        double ret = ThreadLocalRandom.current().nextDouble(left, right);
        return String.format("%.6f", ret).replace(',', '.');
    }
}