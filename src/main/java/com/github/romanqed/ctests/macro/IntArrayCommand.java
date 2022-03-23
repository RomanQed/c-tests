package com.github.romanqed.ctests.macro;

@NamedMacro("i_array")
public class IntArrayCommand extends ArrayCommand {
    public IntArrayCommand() {
        super(new IntCommand());
    }
}
