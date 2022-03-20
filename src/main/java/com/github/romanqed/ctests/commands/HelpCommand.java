package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.util.IOUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HelpCommand extends ConsoleCommand {
    private final Map<String, ConsoleCommand> commands;

    public HelpCommand(Map<String, ConsoleCommand> commands) {
        super("help", "help");
        this.commands = Objects.requireNonNull(commands);
    }

    @Override
    public void handle(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        ConsoleCommand command = commands.get(args.get(0));
        if (command == null) {
            System.out.println("Команда не найдена!");
            return;
        }
        String help = command.getHelp();
        if (help == null) {
            System.out.println("Команда не предоставляет справку!");
            return;
        }
        System.out.println(IOUtil.readResourceFile(help));
    }
}
