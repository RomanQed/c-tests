package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;

import java.util.List;
import java.util.Set;

@NamedCommand("menu")
@Help("menu")
public class MenuCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> args) {
        Set<String> commands = storage.getField(Main.COMMANDS).keySet();
        StringBuilder builder = new StringBuilder("Доступные команды:\n");
        for (String command : commands) {
            builder.append("* ").append(command).append("\n");
        }
        if (commands.isEmpty()) {
            builder.append("* Ни одной команды не найдено!\n");
        }
        System.out.println(builder);
    }
}
