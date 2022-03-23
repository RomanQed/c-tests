package com.github.romanqed.ctests.commands;

import java.util.Collection;
import java.util.List;

public class MenuCommand extends ConsoleCommand {
    private final String menu;

    public MenuCommand(Collection<String> commands) {
        super("menu", "menu");
        StringBuilder builder = new StringBuilder();
        for (String command : commands) {
            builder.append("* ").append(command).append("\n");
        }
        if (commands.isEmpty()) {
            builder.append("* Ни одной команды не найдено!\n");
        }
        menu = builder.toString();
    }

    @Override
    public void handle(List<String> arguments) {
        System.out.println("Доступные команды:");
        System.out.println(menu);
    }
}
