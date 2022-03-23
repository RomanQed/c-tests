package com.github.romanqed.ctests.commands;

import com.github.romanqed.ctests.Main;
import com.github.romanqed.ctests.storage.Storage;
import com.github.romanqed.ctests.storage.StorageProvider;
import com.github.romanqed.ctests.tasks.Task;
import com.github.romanqed.ctests.tests.MarkedTest;
import com.github.romanqed.ctests.tests.TestType;
import com.github.romanqed.ctests.util.ExecUtil;
import com.github.romanqed.ctests.util.IOUtil;

import java.io.IOException;
import java.util.List;

@NamedCommand("run")
@Help("run")
public class RunCommand extends ConsoleCommand {
    private final Storage storage = StorageProvider.getStorage();

    @Override
    public void handle(List<String> arguments) throws Exception {
        Task task = storage.get(Main.TASK);
        if (task == null || !task.getDirectory().exists()) {
            System.out.println("Откройте директорию корректно!");
            storage.remove(Main.TASK);
            return;
        }
        String command = task.getDirectory().getAbsolutePath() + "/" + ExecUtil.APP;
        List<MarkedTest> positives = Utils.positive(task.getTests());
        List<MarkedTest> negatives = Utils.negative(task.getTests());
        for (MarkedTest test : positives) {
            if (runTest(command, test)) {
                break;
            }
        }
        for (MarkedTest test : negatives) {
            if (runTest(command, test)) {
                break;
            }
        }
    }

    private boolean runTest(String command, MarkedTest test) throws IOException, InterruptedException {
        String input = IOUtil.readFile(test.getInput());
        String arguments = "";
        if (test.getArguments() != null) {
            arguments = IOUtil.readFile(test.getArguments());
            int index = arguments.indexOf('\n');
            if (index >= 0) {
                arguments = arguments.substring(0, index);
            }
        }
        ExecUtil.ExecData data = ExecUtil.runProcess(command + " " + arguments, input);
        String output = IOUtil.readFile(test.getOutput()).trim();
        String pattern = "[" + test.getType() + " test #" + test.getNumber() + "] ";
        if (test.getType() == TestType.POSITIVE && data.getCode() != 0) {
            System.out.println(pattern + "Code is not 0");
            return true;
        }
        if (test.getType() == TestType.NEGATIVE && data.getCode() == 0) {
            System.out.println(pattern + "Code is 0");
            return true;
        }
        String received = data.getOutput().trim();
        if (!output.equals(received)) {
            System.out.println(pattern + "\nExpected:\n" + output + "\nReceived:\n" + received);
            return true;
        }
        System.out.println(pattern + "Success");
        return false;
    }
}
