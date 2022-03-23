package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.macro.MacroUtil;
import com.github.romanqed.ctests.util.IOUtil;

import java.util.List;

@NamedCommand("macro")
@Help("macro")
public class MacroCommand extends ConsoleCommand {

    @Override
    public void handle(List<String> arguments) throws Exception {
        String template = IOUtil.readMultiString(Util.STOP_CODE);
        System.out.println(MacroUtil.parseMultiLine(template));
    }
}
