package com.github.romanqed.ctests.macro;

import com.github.romanqed.ctests.util.ReflectionUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MacroUtil {
    public static final List<MacroCommand> COMMANDS;
    static final Pattern MACRO = Pattern.compile("\\{(.)([^:;]+)(?::([^;]+))?};?");

    static {
        try {
            COMMANDS = ReflectionUtil.findAllMacroCommands();
        } catch (Exception e) {
            throw new IllegalStateException("Can't find macro commands", e);
        }
    }

    public static String parseLine(String rawString, MacroProcessor processor) throws Exception {
        Matcher matcher = MACRO.matcher(rawString);
        while (matcher.find()) {
            String rawMacro = matcher.group();
            Macro toInsert = Macro.parse(rawMacro);
            rawString = rawString.replace(rawMacro, processor.execute(toInsert));
        }
        return rawString;
    }

    public static String parseLine(String rawString) throws Exception {
        return parseLine(rawString, new MacroProcessor(COMMANDS));
    }

    public static String parseMultiLine(String rawString) throws Exception {
        MacroProcessor processor = new MacroProcessor(COMMANDS);
        String[] lines = rawString.trim().split("\n");
        StringBuilder ret = new StringBuilder();
        for (String line : lines) {
            line = parseLine(line, processor);
            if (!line.isEmpty()) {
                ret.append(line).append('\n');
            }
        }
        return ret.toString();
    }
}
