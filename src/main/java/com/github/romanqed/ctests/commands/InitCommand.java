package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.tasks.TaskData;

import java.io.File;
import java.util.List;
import java.util.Scanner;

@NamedCommand("init")
@Help("dir_init")
public class InitCommand extends ConsoleCommand {
    private static final Scanner IN = new Scanner(System.in);
    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        String path = arguments.get(0);
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
        storage.set(Main.TASK, task);
    }

    protected String read(String prompt) {
        System.out.print(prompt + ": ");
        return IN.nextLine();
    }
}
