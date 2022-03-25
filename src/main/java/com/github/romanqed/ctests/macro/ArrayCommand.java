package com.github.romanqed.ctests.macro;

import com.github.romanqed.ctests.util.ReflectionUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@NamedMacro("arr")
public class ArrayCommand extends MacroCommand {
    private static final Map<String, MacroCommand> TYPES;

    static {
        try {
            TYPES = Collections.unmodifiableMap(ReflectionUtil.findAllArrayTypes());
        } catch (Exception e) {
            throw new IllegalStateException("Can't find array types", e);
        }
    }

    @Override
    public String execute(List<String> arguments) throws Exception {
        if (arguments.size() < 2) {
            throw new IllegalArgumentException("Invalid array command arguments!");
        }
        MacroCommand type = TYPES.get(arguments.get(0));
        if (type == null) {
            throw new IllegalArgumentException("Invalid array type!");
        }
        int length = Integer.parseInt(arguments.get(1));
        StringBuilder builder = new StringBuilder();
        List<String> commandArguments = arguments.subList(2, arguments.size());
        for (int i = 0; i < length; ++i) {
            builder.append(type.execute(commandArguments)).append(' ');
        }
        String ret = builder.toString();
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }
}
