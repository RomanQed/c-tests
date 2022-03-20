package com.github.romanqed.ctests;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.commands.HelpCommand;
import com.github.romanqed.ctests.commands.MenuCommand;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.ReflectionUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<ConsoleCommand> found = ReflectionUtil.findAllCommands();
        Menu menu = new Menu(found);
        menu.addCommand(new HelpCommand(menu.getCommands()));
        menu.addCommand(new MenuCommand(menu.getCommands().keySet()));
        String logo = IOUtil.readResourceFile("logo");
        System.out.println(logo);
        menu.run();
    }
}
