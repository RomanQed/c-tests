package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.Menu;
import com.github.romanqed.ctests.macro.MacroUtil;
import com.github.romanqed.ctests.storage.Field;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.tests.MarkedTest;
import com.github.romanqed.ctests.tests.TestType;
import com.github.romanqed.ctests.util.ExecUtil;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.ParseUtil;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@NamedCommand("tests")
@Help("test")
public class TestsCommand extends ConsoleCommand {
    private static final Menu MENU;
    protected static final Field<String> MACRO_TEMPLATE = new Field<>("MACRO_TEMPLATE", String.class);

    static {
        List<ConsoleCommand> commands = new LinkedList<>();
        commands.add(new ListCommand());
        commands.add(new InfoCommand());
        commands.add(new EditCommand());
        commands.add(new CreateCommand());
        commands.add(new RemoveCommand());
        commands.add(new TemplateCommand());
        commands.add(new GenerateCommand());
        MENU = new Menu(commands);
        MENU.addCommand(new HelpCommand(MENU.getCommands()));
        MENU.addCommand(new MenuCommand(MENU.getCommands().keySet()));
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

class Util {
    protected static final String POS = "pos";
    protected static final String NEG = "neg";
    protected static final String IN = "in";
    protected static final String OUT = "out";
    protected static final String ARGS = "args";
    protected static final String STOP_CODE = "\\";
    protected static final Scanner SCANNER = new Scanner(System.in);

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
        List<MarkedTest> typed = Util.sort(all, type);
        if (number < 0 || number >= typed.size()) {
            throw new IllegalStateException("Номер теста не существует!");
        }
        return typed.get(number);
    }

    protected static MarkedTest generateTest(Task task, TestType type, boolean needArguments) {
        int number = Util.sort(task.getTests(), type).size() + 1;
        String pattern = task.getDirectory().getAbsolutePath() + "/func_tests/data/" + type.getName() + "_"
                + ParseUtil.formatNumber(number) + "_";
        MarkedTest test = new MarkedTest();
        test.setType(type);
        test.setNumber(number);
        test.setInput(new File(pattern + "in.txt"));
        test.setOutput(new File(pattern + "out.txt"));
        if (needArguments) {
            test.setArguments(new File(pattern + "args.txt"));
        }
        return test;
    }
}

class ListCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public ListCommand() {
        super("list", "test_list");
    }

    @Override
    public void handle(List<String> args) {
        List<MarkedTest> tests = storage.get(Main.TASK).getTests();
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
            showTests(Util.positive(tests));
            System.out.println("===Negative tests");
            showTests(Util.negative(tests));
        } else if (type.equals(Util.POS)) {
            System.out.println("===Positive tests");
            showTests(Util.positive(tests));
        } else if (type.equals(Util.NEG)) {
            System.out.println("===Negative tests");
            showTests(Util.negative(tests));
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
    public void handle(List<String> arguments) throws IOException {
        List<MarkedTest> all = storage.get(Main.TASK).getTests();
        if (all.isEmpty()) {
            System.out.println("Тестов нет!");
            return;
        }
        if (arguments.size() != 2) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        MarkedTest test = Util.findTest(all, Integer.parseInt(arguments.get(1)) - 1, arguments.get(0));
        String in = IOUtil.readFile(test.getInput());
        String out = null;
        if (test.getOutput() != null) {
            out = IOUtil.readFile(test.getOutput());
        }
        String testArguments = null;
        if (test.getArguments() != null) {
            testArguments = IOUtil.readFile(test.getArguments());
        }
        System.out.println("Входные данные:\n" + in);
        if (out != null) {
            System.out.println("Выходные данные:\n" + out);
        }
        if (testArguments != null) {
            System.out.println("Аргументы:\n" + testArguments);
        }
    }
}

class EditCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public EditCommand() {
        super("edit", "test_edit");
    }

    @Override
    public void handle(List<String> arguments) throws IOException {
        List<MarkedTest> all = storage.get(Main.TASK).getTests();
        if (all.isEmpty()) {
            System.out.println("Тестов нет!");
            return;
        }
        if (arguments.size() < 3) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        MarkedTest test = Util.findTest(all, Integer.parseInt(arguments.get(1)) - 1, arguments.get(0));
        for (int i = 2; i < arguments.size(); ++i) {
            String type = arguments.get(i);
            String body = IOUtil.readMultiString(Util.STOP_CODE);
            File file;
            switch (type) {
                case Util.IN:
                    file = test.getInput();
                    break;
                case Util.OUT:
                    file = test.getOutput();
                    break;
                case Util.ARGS:
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
    private static final String AUTO = "auto";
    private final Storage storage = StorageProvider.getStorage();

    public CreateCommand() {
        super("create", "test_create");
    }

    @Override
    public void handle(List<String> arguments) throws IOException, InterruptedException {
        if (arguments.isEmpty() || arguments.size() > 3) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        boolean needArguments = arguments.contains(Util.ARGS);
        boolean auto = arguments.contains(AUTO);
        Task task = storage.get(Main.TASK);
        List<MarkedTest> all = task.getTests();
        MarkedTest test = Util.generateTest(task, TestType.fromName(arguments.get(0)), needArguments);
        String input = IOUtil.readMultiString(Util.STOP_CODE);
        String testArguments = " ";
        if (needArguments) {
            testArguments += Util.SCANNER.nextLine();
            IOUtil.writeFile(test.getArguments(), testArguments);
        }
        String output;
        if (auto) {
            String command = task.getDirectory().getAbsolutePath() + "/" + ExecUtil.APP + testArguments;
            ExecUtil.ExecData data = ExecUtil.runProcess(command, input);
            if (test.getType() == TestType.POSITIVE && data.getCode() != 0) {
                throw new IllegalStateException("The program returned not 0 on a positive test");
            }
            if (test.getType() == TestType.NEGATIVE && data.getCode() == 0) {
                throw new IllegalStateException("The program returned 0 on a negative test");
            }
            output = data.getOutput();
        } else {
            output = IOUtil.readMultiString(Util.STOP_CODE);
        }
        IOUtil.writeFile(test.getInput(), input);
        IOUtil.writeFile(test.getOutput(), output);
        all.add(test);
        System.out.println("Номер нового теста: " + test.getNumber());
    }
}

class RemoveCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public RemoveCommand() {
        super("remove", "test_remove");
    }

    @Override
    public void handle(List<String> arguments) throws IOException {
        if (arguments.size() != 2) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        List<MarkedTest> all = storage.get(Main.TASK).getTests();
        TestType type = TestType.fromName(arguments.get(0));
        int index = Integer.parseInt(arguments.get(1)) - 1;
        List<MarkedTest> typed = Util.sort(all, type);
        if (index < 0 || index >= typed.size()) {
            System.out.println("Неверный индекс!");
            return;
        }
        MarkedTest toRemove = typed.get(index);
        boolean check = removeFile(toRemove.getInput());
        check = check && removeFile(toRemove.getOutput());
        check = check && removeFile(toRemove.getArguments());
        if (!check) {
            System.out.println("Ошибка при удалении!");
            return;
        }
        for (int i = index + 1; i < typed.size(); ++i) {
            MarkedTest test = typed.get(i);
            test.setNumber(test.getNumber() - 1);
            File input = test.getInput();
            File output = test.getOutput();
            File testArguments = test.getArguments();
            test.setInput(renameTestFile(input));
            if (output != null) {
                test.setOutput(renameTestFile(output));
            }
            if (testArguments != null) {
                test.setArguments(renameTestFile(testArguments));
            }
        }
        all.remove(toRemove);
    }

    private File renameTestFile(File source) throws IOException {
        String path = source.getParentFile().getAbsolutePath() + "/";
        File toFile = new File(path + ParseUtil.decreaseIndex(source.getName()));
        if (!source.renameTo(toFile)) {
            throw new IOException("Can't rename test file " + source);
        }
        return toFile;
    }

    private boolean removeFile(File source) {
        if (source == null) {
            return true;
        }
        return source.delete();
    }
}

class TemplateCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public TemplateCommand() {
        super("template", "test_template");
    }

    @Override
    public void handle(List<String> arguments) {
        String template = IOUtil.readMultiString(Util.STOP_CODE);
        storage.set(TestsCommand.MACRO_TEMPLATE, template);
    }
}

class GenerateCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public GenerateCommand() {
        super("generate", "test_generate");
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        String template = storage.get(TestsCommand.MACRO_TEMPLATE);
        if (template == null) {
            System.out.println("Шаблон для тестов не задан!");
            return;
        }
        int count = Integer.parseInt(arguments.get(0));
        Task task = storage.get(Main.TASK);
        List<MarkedTest> tests = task.getTests();
        String command = task.getDirectory().getAbsolutePath() + "/" + ExecUtil.APP;
        for (int i = 0; i < count; ++i) {
            MarkedTest test = Util.generateTest(task, TestType.POSITIVE, false);
            String input = MacroUtil.parseMultiLine(template);
            ExecUtil.ExecData data = ExecUtil.runProcess(command, input);
            if (data.getCode() != 0) {
                throw new IllegalStateException("The program returned not 0 on a positive test");
            }
            IOUtil.writeFile(test.getInput(), input);
            IOUtil.writeFile(test.getOutput(), data.getOutput());
            tests.add(test);
        }
        System.out.println("Успешно добавлено " + count + " тестов!");
    }
}