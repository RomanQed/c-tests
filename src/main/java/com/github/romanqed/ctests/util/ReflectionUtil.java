package com.github.romanqed.ctests.util;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.commands.NamedCommand;
import com.github.romanqed.ctests.macro.MacroCommand;
import com.github.romanqed.ctests.macro.NamedMacro;
import org.atteo.classindex.ClassIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectionUtil {
    public static List<ConsoleCommand> findAllConsoleCommands() throws InstantiationException, IllegalAccessException {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(NamedCommand.class);
        List<ConsoleCommand> ret = new ArrayList<>();
        for (Class<?> clazz : classes) {
            ret.add((ConsoleCommand) clazz.newInstance());
        }
        return ret;
    }

    public static Map<String, MacroCommand> findAllMacroCommands() throws InstantiationException, IllegalAccessException {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(NamedMacro.class);
        Map<String, MacroCommand> ret = new HashMap<>();
        for (Class<?> clazz : classes) {
            ret.put(clazz.getAnnotation(NamedMacro.class).value(), (MacroCommand) clazz.newInstance());
        }
        return ret;
    }
}
