package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.util.IOUtil;

import java.util.List;

@NamedCommand("cat")
@Help("cat")
public class CatCommand extends ConsoleCommand {
    @Override
    public void handle(List<String> args) {
        if (args.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        int count;
        try {
            count = Integer.parseInt(args.get(0));
        } catch (Exception e) {
            System.out.println("Некорректный аргумент!");
            return;
        }
        String cat = IOUtil.readResourceFile("cat_art");
        for (int i = 0; i < count; ++i) {
            System.out.println(cat);
        }
    }
}
