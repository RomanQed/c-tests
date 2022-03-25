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
        if (arguments.size() < 3) {
            throw new IllegalArgumentException("Invalid array command arguments!");
        }
        int length = Integer.parseInt(arguments.get(0));
        String split = arguments.get(1);
        MacroCommand type = TYPES.get(arguments.get(2));
        if (type == null) {
            throw new IllegalArgumentException("Invalid array type!");
        }
        StringBuilder builder = new StringBuilder();
        List<String> commandArguments = arguments.subList(3, arguments.size());
        for (int i = 0; i < length; ++i) {
            builder.append(type.execute(commandArguments)).append(split);
        }
        String ret = builder.toString();
        ret = ret.substring(0, ret.length() - split.length());
        return ret;
    }
}
