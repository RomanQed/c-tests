package com.github.romanqed.ctests.util;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.commands.NamedCommand;
import org.atteo.classindex.ClassIndex;

import java.util.HashMap;
import java.util.Map;

public class ReflectionUtil {
    public static Map<String, ConsoleCommand> findAllCommands() throws InstantiationException, IllegalAccessException {
        Iterable<Class<?>> classes = ClassIndex.getAnnotated(NamedCommand.class);
        Map<String, ConsoleCommand> ret = new HashMap<>();
        for (Class<?> clazz : classes) {
            ConsoleCommand command = (ConsoleCommand) clazz.newInstance();
            ret.put(command.getName(), command);
        }
        return ret;
    }
}
