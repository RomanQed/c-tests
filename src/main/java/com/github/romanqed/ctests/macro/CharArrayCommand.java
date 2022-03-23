package com.github.romanqed.ctests.macro;

@NamedMacro("c_array")
public class CharArrayCommand extends ArrayCommand {
    public CharArrayCommand() {
        super(new CharCommand());
    }
}
