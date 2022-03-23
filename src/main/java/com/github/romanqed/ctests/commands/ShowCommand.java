package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.tasks.TaskData;

import java.util.List;

@NamedCommand("show")
@Help("dir_show")
public class ShowCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> arguments) {
        Task task = storage.get(Main.TASK);
        if (task == null) {
            System.out.println("Нет открытой директории!");
            return;
        }
        if (!task.getDirectory().exists()) {
            System.out.println("Нет открытой директории!");
            storage.remove(Main.TASK);
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
