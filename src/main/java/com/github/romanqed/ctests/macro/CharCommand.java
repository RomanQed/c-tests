package com.github.romanqed.ctests.macro;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NamedMacro("char")
public class CharCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) throws Exception {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Invalid double command arguments!");
        }
        int left = arguments.get(0).charAt(0);
        int right = arguments.get(1).charAt(1);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return "" + Character.forDigit(random.nextInt(left, right), 10);
    }
}
