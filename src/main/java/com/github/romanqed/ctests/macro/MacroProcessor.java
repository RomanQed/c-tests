package com.github.romanqed.ctests.macro;

import com.github.romanqed.jutils.util.Action;

import java.util.*;

class MacroProcessor implements Action<Macro, String> {
    private final Map<String, MacroCommand> commands;
    private final Map<String, String> variables;

    public MacroProcessor(Collection<MacroCommand> commands) {
        Objects.requireNonNull(commands);
        this.commands = new HashMap<>();
        for (MacroCommand command : commands) {
            this.commands.put(command.getName(), command);
        }
        this.variables = new HashMap<>();
    }

    public Map<String, String> getVariables() {
        return Collections.unmodifiableMap(variables);
    }

    public void clearVariables() {
        variables.clear();
    }

    private String processSimpleMacro(Macro macro) throws Exception {
        MacroType type = macro.getType();
        List<String> arguments = macro.getArguments();
        if (type == MacroType.SUBSTITUTION) {
            return Objects.requireNonNull(variables.get(macro.getName()));
        }
        if (type == MacroType.DECLARE) {
            if (arguments.isEmpty()) {
                throw new IllegalArgumentException("Invalid declare macro: " + macro);
            }
            variables.put(macro.getName(), arguments.get(0));
            return "";
        }
        MacroCommand command = commands.get(macro.getName());
        if (command == null) {
            throw new IllegalArgumentException("Invalid command macro: " + macro);
        }
        return command.execute(arguments);
    }

    @Override
    public String execute(Macro macro) throws Exception {
        List<String> arguments = macro.getArguments();
        for (int i = 0; i < arguments.size(); ++i) {
            String argument = arguments.get(i);
            if (Macro.isMacro(argument)) {
                Macro toProcess = Macro.parse(argument);
                argument = execute(toProcess);
            }
            if (argument.isEmpty()) {
                arguments.remove(i);
                --i;
                continue;
            }
            arguments.set(i, argument);
        }
        return processSimpleMacro(macro);
    }
}