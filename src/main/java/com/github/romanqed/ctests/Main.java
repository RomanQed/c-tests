package com.github.romanqed.ctests;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.storage.Field;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.ReflectionUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static final Field<Map<String, ConsoleCommand>> COMMANDS = new Field<>("CMD", Map.class);

    public static void main(String[] args) throws Exception {
        List<ConsoleCommand> found = ReflectionUtil.findAllCommands();
        Map<String, ConsoleCommand> commands = new ConcurrentHashMap<>();
        found.forEach(e -> commands.put(e.getName(), e));
        StorageProvider.getStorage().setField(COMMANDS, commands);
        Menu menu = new Menu(found);
        String logo = IOUtil.readResourceFile("logo");
        System.out.println(logo);
        menu.run();
    }
}
