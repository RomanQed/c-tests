package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.util.IOUtil;

import java.util.List;
import java.util.Map;

@NamedCommand("help")
@Help("help")
public class HelpCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        Map<String, ConsoleCommand> commands = storage.getField(Main.COMMANDS);
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
