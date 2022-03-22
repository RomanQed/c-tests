package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Menu;
import com.github.romanqed.ctests.storage.Field;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.tasks.TaskData;
import com.github.romanqed.ctests.util.InvalidTaskDirectoryException;
import com.github.romanqed.ctests.util.InvalidTestException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@NamedCommand("dir")
@Help("dir")
public class DirectoryCommand extends ConsoleCommand {
    static final Field<Task> TASK = new Field<>("TASK", Task.class);
    private static final Menu MENU;

    static {
        List<ConsoleCommand> commands = new LinkedList<>();
        commands.add(new OpenCommand());
        commands.add(new ShowCommand());
        commands.add(new InitCommand());
        MENU = new Menu(commands);
        MENU.addCommand(new HelpCommand(MENU.getCommands()));
        MENU.addCommand(new MenuCommand(MENU.getCommands().keySet()));
    }

    @Override
    public void handle(List<String> args) {
        System.out.println("Подменю для работы с директорией");
        MENU.run();
    }
}

class OpenCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public OpenCommand() {
        super("open", "dir_open");
    }

    @Override
    public void handle(List<String> args) throws InvalidTestException, InvalidTaskDirectoryException {
        if (args.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        String path = args.get(0);
        File directory = new File(path);
        Task task = Task.openDirectory(directory);
        storage.set(DirectoryCommand.TASK, task);
    }
}

class ShowCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public ShowCommand() {
        super("show", "dir_show");
    }

    @Override
    public void handle(List<String> args) {
        Task task = storage.get(DirectoryCommand.TASK);
        if (task == null) {
            System.out.println("Нет открытой директории!");
            return;
        }
        if (!task.getDirectory().exists()) {
            System.out.println("Нет открытой директории!");
            storage.remove(DirectoryCommand.TASK);
            return;
        }
        TaskData data = task.getData();
        System.out.println("Полный путь: " + task.getDirectory());
        System.out.println("===Информация");
        System.out.println("Номер лабы: " + data.getLabNumber());
        System.out.println("Номер задачи: " + data.getNumber());
        System.out.println("Вариант: " + data.getVariant());
        System.out.println("===");
        System.out.println("Тесты есть: " + !task.getTests().isEmpty());
    }
}

class InitCommand extends ConsoleCommand {
    private static final Scanner in = new Scanner(System.in);
    private final Storage storage = StorageProvider.getStorage();

    public InitCommand() {
        super("init", "dir_init");
    }

    @Override
    public void handle(List<String> args) throws Exception {
        if (args.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        String path = args.get(0);
        File parent = new File(path);
        if (!(parent.exists() && parent.isDirectory())) {
            System.out.println("Невозможно создать рабочую папку по такому пути!");
            return;
        }
        TaskData data = new TaskData();
        data.setLabNumber(Integer.parseInt(read("Введите номер лабы")));
        data.setNumber(Integer.parseInt(read("Введите номер задачи")));
        data.setVariant(Integer.parseInt(read("Введите номер варианта или -1")));
        Task task = Task.createDirectory(parent, data);
        storage.set(DirectoryCommand.TASK, task);
    }

    protected String read(String prompt) {
        System.out.print(prompt + ": ");
        return in.nextLine();
    }
}
