package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Menu;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.tests.MarkedTest;
import com.github.romanqed.ctests.tests.TestType;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.ParseUtil;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@NamedCommand("test")
@Help("test")
public class TestCommand extends ConsoleCommand {
    private static final Menu menu;

    static {
        List<ConsoleCommand> commands = new LinkedList<>();
        commands.add(new ListCommand());
        commands.add(new InfoCommand());
        commands.add(new EditCommand());
        commands.add(new CreateCommand());
        commands.add(new DraftCommand());
        commands.add(new ReadmeCommand());
        commands.add(new RemoveCommand());
        menu = new Menu(commands);
        menu.addCommand(new HelpCommand(menu.getCommands()));
        menu.addCommand(new MenuCommand(menu.getCommands().keySet()));
        menu.onExit(() -> {
            // TODO
        });
    }

    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> args) {
        Task task = storage.get(DirectoryCommand.TASK);
        if (task == null || !task.getDirectory().exists()) {
            System.out.println("Откройте директорию корректно!");
            storage.remove(DirectoryCommand.TASK);
            return;
        }
        menu.run();
    }
}

class Utils {
    protected static final String POS = "pos";
    protected static final String NEG = "neg";
    protected static final String IN = "in";
    protected static final String OUT = "out";
    protected static final String ARGS = "args";
    protected static final String STOP_CODE = "\\";

    protected static List<MarkedTest> sort(List<MarkedTest> all, TestType type) {
        return all.stream().
                filter(e -> e.getType() == type).
                sorted(Comparator.comparingInt(MarkedTest::getNumber)).
                collect(Collectors.toList());
    }

    protected static List<MarkedTest> positive(List<MarkedTest> all) {
        return sort(all, TestType.POSITIVE);
    }

    protected static List<MarkedTest> negative(List<MarkedTest> all) {
        return sort(all, TestType.NEGATIVE);
    }

    protected static MarkedTest findTest(List<MarkedTest> all, int number, String rawType) {
        TestType type;
        try {
            type = TestType.fromName(rawType);
        } catch (Exception e) {
            throw new IllegalStateException("Не существующий тип!");
        }
        List<MarkedTest> typed = Utils.sort(all, type);
        if (number < 0 || number >= typed.size()) {
            throw new IllegalStateException("Номер теста не существует!");
        }
        return typed.get(number);
    }
}

class ListCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public ListCommand() {
        super("list", "test_list");
    }

    @Override
    public void handle(List<String> args) {
        List<MarkedTest> tests = storage.get(DirectoryCommand.TASK).getTests();
        if (tests.isEmpty()) {
            System.out.println("Тестов не существует!");
            return;
        }
        String type = null;
        if (args.size() == 1) {
            type = args.get(0);
        }
        if (type == null) {
            System.out.println("===Positive tests");
            showTests(Utils.positive(tests));
            System.out.println("===Negative tests");
            showTests(Utils.negative(tests));
        } else if (type.equals(Utils.POS)) {
            System.out.println("===Positive tests");
            showTests(Utils.positive(tests));
        } else if (type.equals(Utils.NEG)) {
            System.out.println("===Positive tests");
            showTests(Utils.positive(tests));
        } else {
            System.out.println("Неверный тип!");
        }
    }

    private void showTests(List<MarkedTest> tests) {
        for (MarkedTest test : tests) {
            String toShow = "Номер: " + test.getNumber() +
                    " In: " + test.getInput().getName();
            if (test.getOutput() != null) {
                toShow += " Out: " + test.getOutput().getName();
            }
            if (test.getArguments() != null) {
                toShow += " Args: " + test.getArguments().getName();
            }
            System.out.println(toShow);
        }
    }
}

class InfoCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public InfoCommand() {
        super("info", "test_info");
    }

    @Override
    public void handle(List<String> args) throws IOException {
        List<MarkedTest> all = storage.get(DirectoryCommand.TASK).getTests();
        if (all.isEmpty()) {
            System.out.println("Тестов нет!");
            return;
        }
        if (args.size() != 2) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        MarkedTest test = Utils.findTest(all, Integer.parseInt(args.get(1)) - 1, args.get(0));
        String in = IOUtil.readFile(test.getInput());
        String out = null;
        if (test.getOutput() != null) {
            out = IOUtil.readFile(test.getOutput());
        }
        String arguments = null;
        if (test.getArguments() != null) {
            arguments = IOUtil.readFile(test.getArguments());
        }
        System.out.println("Входные данные:\n" + in);
        if (out != null) {
            System.out.println("Выходные данные:\n" + out);
        }
        if (arguments != null) {
            System.out.println("Аргументы:\n" + arguments);
        }
    }
}

class EditCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public EditCommand() {
        super("edit", "test_edit");
    }

    @Override
    public void handle(List<String> args) throws IOException {
        List<MarkedTest> all = storage.get(DirectoryCommand.TASK).getTests();
        if (all.isEmpty()) {
            System.out.println("Тестов нет!");
            return;
        }
        if (args.size() < 3) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        MarkedTest test = Utils.findTest(all, Integer.parseInt(args.get(1)) - 1, args.get(0));
        for (int i = 2; i < args.size(); ++i) {
            String type = args.get(i);
            String body = IOUtil.readMultiString(Utils.STOP_CODE);
            File file;
            switch (type) {
                case Utils.IN:
                    file = test.getInput();
                    break;
                case Utils.OUT:
                    file = test.getOutput();
                    break;
                case Utils.ARGS:
                    file = test.getArguments();
                    break;
                default:
                    System.out.println("Неверный тип: " + type);
                    continue;
            }
            IOUtil.writeFile(file, body);
        }
    }
}

class CreateCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public CreateCommand() {
        super("create", "test_create");
    }

    @Override
    public void handle(List<String> args) throws IOException {
        Task task = storage.get(DirectoryCommand.TASK);
        List<MarkedTest> all = task.getTests();
        if (args.isEmpty() || args.size() > 3) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        boolean out = args.size() >= 2 && args.get(1).equals(Utils.OUT);
        boolean arguments = args.size() == 3 && args.get(2).equals(Utils.ARGS);
        TestType type = TestType.fromName(args.get(0));
        int number = Utils.sort(all, type).size() + 1;
        String pattern = task.getDirectory().getAbsolutePath() + "/func_tests/data/" + type.getName() + "_"
                + ParseUtil.formatNumber(number) + "_";
        MarkedTest test = new MarkedTest();
        test.setType(type);
        test.setNumber(number);
        File input = new File(pattern + "in.txt");
        IOUtil.writeFile(input, IOUtil.readMultiString(Utils.STOP_CODE));
        test.setInput(input);
        if (out) {
            File output = new File(pattern + "out.txt");
            IOUtil.writeFile(output, IOUtil.readMultiString(Utils.STOP_CODE));
            test.setOutput(output);
        }
        if (arguments) {
            File argsFile = new File(pattern + "args.txt");
            IOUtil.writeFile(argsFile, IOUtil.readMultiString(Utils.STOP_CODE));
            test.setArguments(argsFile);
        }
        all.add(test);
        System.out.println("Номер нового теста: " + number);
    }
}

class RemoveCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public RemoveCommand() {
        super("remove", "test_remove");
    }

    @Override
    public void handle(List<String> args) {
        List<MarkedTest> all = storage.get(DirectoryCommand.TASK).getTests();
        if (args.size() != 2) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        MarkedTest test = Utils.findTest(all, Integer.parseInt(args.get(1)) - 1, args.get(0));
        if (test.getInput() != null && test.getInput().delete()) {
            System.out.println("Входной файл успешно удалён!");
        }
        if (test.getOutput() != null && test.getOutput().delete()) {
            System.out.println("Выходной файл успешно удалён!");
        }
        if (test.getArguments() != null && test.getArguments().delete()) {
            System.out.println("Файл аргументов успешно удалён!");
        }
        all.remove(test);
    }
}

class DraftCommand extends ConsoleCommand {
    public DraftCommand() {
        super("draft");
    }

    @Override
    public void handle(List<String> args) {

    }
}

class ReadmeCommand extends ConsoleCommand {
    public ReadmeCommand() {
        super("readme");
    }

    @Override
    public void handle(List<String> args) {

    }
}