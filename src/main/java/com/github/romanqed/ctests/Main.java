package com.github.romanqed.ctests;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.ReflectionUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        List<ConsoleCommand> commands = ReflectionUtil.findAllCommands();
        Menu menu = new Menu(commands);
        String logo = IOUtil.readResourceFile("logo");
        System.out.println(logo);
        menu.run();
    }
}
