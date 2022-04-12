package com.github.romanqed.ctests;

import com.github.romanqed.ctests.commands.ConsoleCommand;
import com.github.romanqed.ctests.commands.HelpCommand;
import com.github.romanqed.ctests.commands.MenuCommand;
import com.github.romanqed.ctests.mongo.MongoRepository;
import com.github.romanqed.ctests.storage.Field;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.ReflectionUtil;
import com.github.romanqed.jutils.concurrent.ThreadTaskFabric;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static final Field<Task> TASK = new Field<>("TASK", Task.class);
    private static final String DATABASE = "ctests";

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            initMongo(args[0]);
        }
        List<ConsoleCommand> found = ReflectionUtil.findAllConsoleCommands();
        Menu menu = new Menu(found);
        menu.addCommand(new HelpCommand(menu.getCommands()));
        menu.addCommand(new MenuCommand(menu.getCommands().keySet()));
        String logo = IOUtil.readResourceFile("logo");
        System.out.println(logo);
        menu.run();
    }

    public static void initMongo(String connectionString) {
        MongoClient mongoClient = MongoClients.create(Objects.requireNonNull(connectionString));
        MongoRepository.init(mongoClient, DATABASE);
        ExecutorService executor = Executors.newWorkStealingPool();
        MongoRepository.setFabric(new ThreadTaskFabric(executor));
    }
}