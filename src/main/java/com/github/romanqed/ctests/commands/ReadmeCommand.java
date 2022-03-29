package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.Menu;
import com.github.romanqed.ctests.storage.Field;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.tests.TestType;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.ParseUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@NamedCommand("readme")
@Help("readme")
public class ReadmeCommand extends ConsoleCommand {
    public static final Field<Draft> DRAFT = new Field<>("DRAFT", Draft.class);
    private static final Menu MENU;

    static {
        List<ConsoleCommand> commands = new LinkedList<>();
        commands.add(new AddCommand());
        commands.add(new ClearCommand());
        commands.add(new ReadmeCreateCommand());
        commands.add(new ReadmeRemoveCommand());
        MENU = new Menu(commands);
        MENU.addCommand(new HelpCommand(MENU.getCommands()));
        MENU.addCommand(new MenuCommand(MENU.getCommands().keySet()));
        MENU.onExit(() -> {
            Storage storage = StorageProvider.getStorage();
            storage.remove(DRAFT);
        });
    }

    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> arguments) {
        Task task = storage.get(Main.TASK);
        if (task == null || !task.getDirectory().exists()) {
            System.out.println("Откройте директорию корректно!");
            storage.remove(Main.TASK);
            return;
        }
        MENU.run();
    }
}

class Draft {
    Map<String, String> positive = new HashMap<>();
    Map<String, String> negative = new HashMap<>();
}

class AddCommand extends ConsoleCommand {
    private static final String PATTERN = "<p>(\\d): \\m</p>";
    private final Storage storage = StorageProvider.getStorage();

    public AddCommand() {
        super("add", "readme_add");
    }

    @Override
    public void handle(List<String> arguments) {
        if (arguments.size() != 3) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        Draft draft = storage.get(ReadmeCommand.DRAFT);
        if (draft == null) {
            draft = new Draft();
            storage.set(ReadmeCommand.DRAFT, draft);
        }
        TestType type = TestType.fromName(arguments.get(0));
        String range = arguments.get(1);
        String message = PATTERN.replace("\\d", arguments.get(1)).replace("\\m", arguments.get(2));
        if (type == TestType.POSITIVE) {
            draft.positive.put(range, message);
        } else {
            draft.negative.put(range, message);
        }
    }
}

class ReadmeRemoveCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public ReadmeRemoveCommand() {
        super("remove", "readme_remove");
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.size() != 2) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        Draft draft = storage.get(ReadmeCommand.DRAFT);
        if (draft == null) {
            System.out.println("Черновик не заполнен!");
            return;
        }
        TestType type = TestType.fromName(arguments.get(0));
        String range = arguments.get(1);
        if (type == TestType.POSITIVE) {
            draft.positive.remove(range);
        } else {
            draft.negative.remove(range);
        }
    }
}

class ClearCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public ClearCommand() {
        super("clear", "readme_clear");
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (storage.get(ReadmeCommand.DRAFT) == null) {
            System.out.println("Черновик не заполнен!");
            return;
        }
        storage.set(ReadmeCommand.DRAFT, new Draft());
    }
}

class ReadmeCreateCommand extends ConsoleCommand {
    private final static String NO_TESTS = "<p>Тестов нет</p>";
    private final Storage storage = StorageProvider.getStorage();

    public ReadmeCreateCommand() {
        super("create", "readme_create");
    }

    @Override
    public void handle(List<String> arguments) throws IOException {
        Draft draft = storage.get(ReadmeCommand.DRAFT);
        Task task = storage.get(Main.TASK);
        File directory = task.getDirectory();
        if (draft == null) {
            System.out.println("Черновик не заполнен!");
            return;
        }
        String pattern = IOUtil.readResourceFile("readme_pattern");
        pattern = pattern.replace("\\#", ParseUtil.formatNumber(task.getData().getLabNumber()));
        if (draft.positive.isEmpty()) {
            pattern = pattern.replace("\\p", NO_TESTS);
        } else {
            pattern = pattern.replace("\\p", draftToString(draft.positive));
        }
        if (draft.negative.isEmpty()) {
            pattern = pattern.replace("\\n", NO_TESTS);
        } else {
            pattern = pattern.replace("\\n", draftToString(draft.negative));
        }
        File readme = new File(directory.getAbsolutePath() + "/func_tests/readme.md");
        IOUtil.writeFile(readme, pattern);
    }

    private int parseRange(String rawRange) {
        rawRange = rawRange.trim().replaceAll("\\s+", " ");
        int pos = rawRange.indexOf('-');
        if (pos < 0) {
            return Integer.parseInt(rawRange);
        }
        return Integer.parseInt(rawRange.substring(0, pos));
    }

    private String draftToString(Map<String, String> draft) {
        Set<Map.Entry<String, String>> entries = draft.entrySet();
        List<Map.Entry<String, String>> list = entries.stream().
                sorted(Comparator.comparingInt(e -> parseRange(e.getKey()))).
                collect(Collectors.toList());
        StringBuilder ret = new StringBuilder();
        for (Map.Entry<String, String> entry : list) {
            ret.append(entry.getValue()).append('\n');
        }
        return ret.toString();
    }
}
