package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Menu;
import com.github.romanqed.ctests.macro.MacroUtil;
import com.github.romanqed.ctests.storage.Field;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.jutils.util.Checks;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@NamedCommand("macros")
@Help("macros")
public class MacrosCommand extends ConsoleCommand {
    protected static final Field<Map<String, String>> MACRO_TABLE = new Field<>("MACRO_TABLE", Map.class);
    private static final Menu MENU;
    protected static File PATH;

    static {
        StorageProvider.getStorage().set(MACRO_TABLE, new ConcurrentHashMap<>());
        List<ConsoleCommand> commands = new LinkedList<>();
        commands.add(new LoadCommand());
        commands.add(new CreateMacroCommand());
        commands.add(new RemoveMacroCommand());
        commands.add(new RunMacroCommand());
        commands.add(new ListMacroCommand());
        MENU = new Menu(commands);
        MENU.addCommand(new HelpCommand(MENU.getCommands()));
        MENU.addCommand(new MenuCommand(MENU.getCommands().keySet()));
    }

    @Override
    public void handle(List<String> arguments) {
        if (arguments.isEmpty() && PATH == null) {
            PATH = new File(".");
        } else if (arguments.size() == 1) {
            PATH = new File(arguments.get(0));
        }
        Checks.requireCorrectValue(PATH, File::isDirectory);
        MENU.run();
    }
}

class LoadCommand extends ConsoleCommand {
    private static final Pattern MACRO_FILE = Pattern.compile(".*\\.qm");

    private final Storage storage = StorageProvider.getStorage();

    public LoadCommand() {
        super("load");
    }

    private void loadMacro(File toLoad) throws IOException {
        String template = IOUtil.readFile(toLoad);
        try {
            String out = MacroUtil.parseMultiLine(template);
            System.out.println(toLoad.getName() + " loaded, output:\n" + out);
        } catch (Exception e) {
            System.out.println("Invalid macro: " + e.getMessage());
            return;
        }
        storage.get(MacrosCommand.MACRO_TABLE).put(IOUtil.splitFileName(toLoad)[0], template);
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.size() == 1) {
            File toLoad = new File(MacrosCommand.PATH.getAbsolutePath() + "/" + arguments.get(0));
            loadMacro(toLoad);
            return;
        }
        Map<String, File> found = IOUtil.findAllFiles(MacrosCommand.PATH, MACRO_FILE);
        for (File macro : found.values()) {
            loadMacro(macro);
        }
    }
}

class CreateMacroCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public CreateMacroCommand() {
        super("create");
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        File toSave = new File(MacrosCommand.PATH.getAbsolutePath() + "/" + arguments.get(0));
        System.out.println("Input macro to save: ");
        String template = IOUtil.readMultiString(Util.STOP_CODE);
        try {
            String out = MacroUtil.parseMultiLine(template);
            System.out.println("Macro processed:\n" + out);
        } catch (Exception e) {
            System.out.println("Invalid macro: " + e.getMessage());
            return;
        }
        IOUtil.writeFile(toSave, template);
        storage.get(MacrosCommand.MACRO_TABLE).put(IOUtil.splitFileName(toSave)[0], template);
    }
}

class RemoveMacroCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public RemoveMacroCommand() {
        super("remove");
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.size() != 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        storage.get(MacrosCommand.MACRO_TABLE).remove(arguments.get(0));
    }
}

class RunMacroCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public RunMacroCommand() {
        super("run");
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.isEmpty()) {
            String template = IOUtil.readMultiString(Util.STOP_CODE);
            System.out.println(MacroUtil.parseMultiLine(template));
            return;
        }
        for (String argument : arguments) {
            String template = storage.get(MacrosCommand.MACRO_TABLE).get(argument);
            if (template == null) {
                continue;
            }
            System.out.println("===" + argument);
            try {
                System.out.println(MacroUtil.parseMultiLine(template));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

class ListMacroCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    public ListMacroCommand() {
        super("list");
    }

    @Override
    public void handle(List<String> arguments) throws Exception {
        Map<String, String> macros = storage.get(MacrosCommand.MACRO_TABLE);
        for (String macro : macros.keySet()) {
            System.out.println("* " + macro);
        }
    }
}