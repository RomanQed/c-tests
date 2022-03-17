package com.github.romanqed.ctests;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.util.ParseUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Menu implements Runnable {
    private static final char SPLIT = ' ';
    private static final String EXIT = "exit";
    private static final String NOT_FOUND = "Command not found!";

    private final String prompt;
    private final Scanner scanner;
    private final Map<String, ConsoleCommand> commands;
    private final AtomicBoolean flag;

    public Menu(String prompt, Collection<ConsoleCommand> commands) {
        Objects.requireNonNull(commands);
        this.prompt = Objects.requireNonNull(prompt);
        this.commands = new ConcurrentHashMap<>();
        this.flag = new AtomicBoolean(false);
        this.scanner = new Scanner(System.in);
        for (ConsoleCommand command : commands) {
            this.commands.put(command.getName(), command);
        }
    }

    public Menu(Collection<ConsoleCommand> commands) {
        this(": ", commands);
    }

    private Data parseRawString(String rawString) {
        Data ret = new Data();
        int pos = rawString.indexOf(SPLIT);
        if (pos < 0) {
            ret.command = rawString;
            ret.arguments = new ArrayList<>();
            return ret;
        }
        ret.command = rawString.substring(0, pos);
        ret.arguments = ParseUtil.parseArguments(rawString.substring(pos + 1));
        return ret;
    }

    public void stop() {
        flag.set(false);
    }

    @Override
    public void run() {
        flag.set(true);
        while (flag.get()) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.equals(EXIT)) {
                flag.set(false);
                break;
            }
            Data data = parseRawString(input);
            ConsoleCommand command = commands.get(data.command);
            if (command == null) {
                System.out.println(NOT_FOUND);
                continue;
            }
            try {
                command.handle(data.arguments);
            } catch (Exception e) {
                System.out.println("Error during command execution: " + e.getMessage());
            }
        }
    }

    private static class Data {
        String command;
        List<String> arguments;
    }
}
