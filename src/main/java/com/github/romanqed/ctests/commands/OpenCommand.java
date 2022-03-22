package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.util.InvalidTaskDirectoryException;
import com.github.romanqed.ctests.util.InvalidTestException;

import java.io.File;
import java.util.List;

@NamedCommand("open")
@Help("dir_open")
public class OpenCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> args) throws InvalidTestException, InvalidTaskDirectoryException {
        if (args.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        String path = args.get(0);
        File directory = new File(path);
        Task task = Task.openDirectory(directory);
        storage.set(Main.TASK, task);
    }
}
