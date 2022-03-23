package com.github.romanqed.ctests.util;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.commands.NamedCommand;
import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;
import org.atteo.classindex.ClassIndex;

import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {
    public static List<ConsoleCommand> findAllConsoleCommands() throws InstantiationException, IllegalAccessException {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(NamedCommand.class);
        List<ConsoleCommand> ret = new ArrayList<>();
        for (Class<?> clazz : classes) {
            ret.add((ConsoleCommand) clazz.newInstance());
        }
        return ret;
    }

    public static List<MacroCommand> findAllMacroCommands() throws InstantiationException, IllegalAccessException {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(NamedMacro.class);
        List<MacroCommand> ret = new ArrayList<>();
        for (Class<?> clazz : classes) {
            ret.add((MacroCommand) clazz.newInstance());
        }
        return ret;
    }
}
