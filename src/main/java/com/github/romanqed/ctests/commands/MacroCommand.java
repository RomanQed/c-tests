package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.macro.MacroUtil;
import com.github.romanqed.ctests.storage.Field;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.util.IOUtil;

import java.util.List;

@NamedCommand("macro")
@Help("macro")
public class MacroCommand extends ConsoleCommand {
    private static final String SAVE = "save";
    private static final String LOAD = "load";
    private static final Field<String> MACRO = new Field<>("MACRO", String.class);

    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> arguments) throws Exception {
        if (arguments.size() > 1) {
            System.out.println("Неверное количество аргументов!");
            return;
        }
        String template;
        if (arguments.contains(LOAD)) {
            template = storage.get(MACRO);
            if (template == null) {
                System.out.println("Нет сохранённого макроса!");
                return;
            }
        } else {
            template = IOUtil.readMultiString(Util.STOP_CODE);
            if (arguments.contains(SAVE)) {
                storage.set(MACRO, template);
            }
        }
        System.out.println("Результат подстановки:");
        System.out.println(MacroUtil.parseMultiLine(template));
    }
}
