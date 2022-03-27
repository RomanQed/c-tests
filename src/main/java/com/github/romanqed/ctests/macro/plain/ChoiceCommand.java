package com.github.romanqed.ctests.macro.plain;

import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@NamedMacro("che")
public class ChoiceCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) {
        if (arguments.isEmpty()) {
            return "";
        }
        int index = ThreadLocalRandom.current().nextInt(0, arguments.size());
        return arguments.get(index);
    }
}
