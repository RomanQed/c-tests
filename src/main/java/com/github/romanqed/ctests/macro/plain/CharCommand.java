package com.github.romanqed.ctests.macro.plain;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NamedMacro("char")
public class CharCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid double command arguments!");
        }
        int left = arguments.get(0).charAt(0);
        int right = arguments.get(1).charAt(0);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return "" + ((char) random.nextInt(left, right));
    }
}
