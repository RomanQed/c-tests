package com.github.romanqed.ctests.commands;

import java.util.List;

@NamedCommand("about")
@Help("about")
public class AboutCommand extends ConsoleCommand {
    @Override
    public void handle(List<String> args) throws Exception {
        System.out.println("Программа - невкусная, автор - любит пиво \"Старый мельник\".");
    }
}
