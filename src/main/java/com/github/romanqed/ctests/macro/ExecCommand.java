package com.github.romanqed.ctests.macro;

import java.util.List;

@NamedMacro("ec")
public class ExecCommand extends MacroCommand {
    @Override
    public String execute(List<String> arguments) throws Exception {
        if (arguments.size() != 1) {
            throw new IllegalArgumentException("Invalid exec command arguments!");
        }
        String source = arguments.get(0)
                .replace("\"", "")
                .replace(",", ";");
        return MacroUtil.parseMultiLine(source);
    }
}
