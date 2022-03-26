package com.github.romanqed.ctests.tasks;

import com.github.romanqed.ctests.tests.MarkedTest;
import com.github.romanqed.ctests.util.IOUtil;
import com.github.romanqed.ctests.util.InvalidTaskDirectoryException;
import com.github.romanqed.ctests.util.InvalidTestException;
import com.github.romanqed.ctests.util.ParseUtil;
import com.github.romanqed.jutils.util.Handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    private static final String TEST_DATA = "/func_tests/data";
    private static final String SOURCE_FILE = "/main.c";
    private static final Pattern LAB_TASK = Pattern.compile("lab_(\\d+)_(\\d+)(?:_(\\d+))?");
    private final File directory;
    private TaskData data;
    private List<MarkedTest> tests;

    private Task(File directory) throws InvalidTaskDirectoryException {
        Objects.requireNonNull(directory);
        if (!(directory.exists() && directory.isDirectory())) {
            throw new InvalidTaskDirectoryException(directory);
        }
        this.directory = directory;
    }

    private Task(File directory, TaskData data) throws InvalidTaskDirectoryException {
        this(directory);
        Objects.requireNonNull(data);
        this.data = data;
    }

    public static Task openDirectory(File directory) throws InvalidTaskDirectoryException, InvalidTestException {
        Task ret = new Task(directory);
        Matcher matcher = LAB_TASK.matcher(directory.getName());
        if (!matcher.matches()) {
            throw new InvalidTaskDirectoryException(directory);
        }
        TaskData data = new TaskData();
        data.setLabNumber(Integer.parseInt(matcher.group(1)));
        data.setNumber(Integer.parseInt(matcher.group(2)));
        String rawVariant = matcher.group(3);
        int variant;
        if (rawVariant == null) {
            variant = -1;
        } else {
            variant = Integer.parseInt(rawVariant);
        }
        data.setVariant(variant);
        ret.data = data;
        File testDirectory = new File(directory.getAbsolutePath() + TEST_DATA);
        if (testDirectory.exists() && testDirectory.isDirectory()) {
            ret.tests = IOUtil.findAllTests(testDirectory);
        } else {
            ret.tests = new ArrayList<>();
        }
        return ret;
    }

    public static Task createDirectory(File parent, TaskData data, Handler<File> handler) throws Exception {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(data);
        if (!(parent.exists() && parent.isDirectory())) {
            throw new IllegalStateException("Invalid parent directory!");
        }
        String name = parent.getAbsolutePath() +
                "/lab_" + ParseUtil.formatNumber(data.labNumber) + "_" + ParseUtil.formatNumber(data.number);
        if (data.variant >= 0) {
            name += "_" + ParseUtil.formatNumber(data.variant);
        }
        File directory = new File(name);
        if (!directory.mkdir()) {
            throw new IllegalStateException("Can't create task directory " + directory);
        }
        File testsDirectory = new File(name + TEST_DATA);
        if (!testsDirectory.mkdirs()) {
            throw new IllegalStateException("Can't create tests directory " + testsDirectory);
        }
        File main = new File(directory.getAbsolutePath() + SOURCE_FILE);
        if (!main.createNewFile()) {
            throw new IllegalStateException("Can't create main source code file!");
        }
        if (handler != null) {
            handler.handle(directory);
        }
        Task ret = new Task(directory, data);
        ret.tests = new ArrayList<>();
        return ret;
    }

    public static Task createDirectory(File parent, TaskData data) throws Exception {
        return createDirectory(parent, data, null);
    }

    public static Task createDirectory(TaskData data) throws Exception {
        return createDirectory(new File("."), data, null);
    }

    public TaskData getData() {
        return data;
    }

    public List<MarkedTest> getTests() {
        return tests;
    }

    public File getDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
