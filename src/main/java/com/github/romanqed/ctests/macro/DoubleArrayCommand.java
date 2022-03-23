package com.github.romanqed.ctests.macro;

@NamedMacro("d_array")
public class DoubleArrayCommand extends ArrayCommand {
    public DoubleArrayCommand() {
        super(new DoubleCommand());
    }
}
