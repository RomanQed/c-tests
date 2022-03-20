package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Menu;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;

import java.util.LinkedList;
import java.util.List;

@NamedCommand("test")
@Help("test")
public class TestCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();
    private static final Menu menu;

    static {
        List<ConsoleCommand> commands = new LinkedList<>();

        menu = new Menu(commands);
        menu.addCommand(new HelpCommand(menu.getCommands()));
        menu.addCommand(new MenuCommand(menu.getCommands().keySet()));
    }

    @Override
    public void handle(List<String> args) throws Exception {
        Task task = storage.getField(DirectoryCommand.TASK);
        if (task == null || !task.getDirectory().exists()) {
            System.out.println("Откройте директорию корректно!");
            storage.setField(DirectoryCommand.TASK, null);
            return;
        }
    }
}

class ListCommand extends ConsoleCommand {
    public ListCommand() {
        super("list");
    }

    @Override
    public void handle(List<String> args) {

    }
}

class InfoCommand extends ConsoleCommand {
    public InfoCommand() {
        super("info");
    }

    @Override
    public void handle(List<String> args) {

    }
}

class EditCommand extends ConsoleCommand {
    public EditCommand() {
        super("edit");
    }

    @Override
    public void handle(List<String> args) {

    }
}

class CreateCommand extends ConsoleCommand {
    public CreateCommand() {
        super("create");
    }

    @Override
    public void handle(List<String> args) {

    }
}