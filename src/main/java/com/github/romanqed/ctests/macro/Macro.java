package com.github.romanqed.ctests.macro;

import com.github.romanqed.jutils.util.BracketTokenizer;
import org.apache.commons.text.StringEscapeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class Macro {
    private static final char OPEN = '(';
    private static final char CLOSE = ')';
    private static final String BLANK = " ";
    private static final BracketTokenizer TOKENIZER = new BracketTokenizer(OPEN, CLOSE, BLANK);
    private final MacroType type;
    private final String name;
    private final List<String> arguments;

    public Macro(MacroType type, String name, List<String> arguments) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        this.arguments = Objects.requireNonNull(arguments);
    }

    public static Macro parse(String rawMacro) throws ParseException {
        Matcher matcher = MacroUtil.MACRO.matcher(rawMacro);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid macro string: " + rawMacro);
        }
        MacroType type = MacroType.fromKey(matcher.group(1).charAt(0));
        String name = matcher.group(2);
        String rawArguments = matcher.group(3);
        List<String> arguments;
        if (rawArguments == null) {
            arguments = new ArrayList<>();
        } else {
            arguments = TOKENIZER.tokenize(rawArguments);
            arguments.replaceAll(StringEscapeUtils::unescapeJava);
        }
        return new Macro(type, name, arguments);
    }

    public static boolean isMacro(String string) {
        return MacroUtil.MACRO.matcher(string).matches();
    }

    public MacroType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return type + ":" + name + ":" + arguments;
    }
}
