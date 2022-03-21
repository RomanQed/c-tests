package com.github.romanqed.ctests.util;

import com.github.romanqed.ctests.tests.MarkedTest;
import com.github.romanqed.ctests.tests.TestType;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOUtil {
    private static final Pattern IN = Pattern.compile("(pos|neg)_(\\d+)_in\\.txt");
    private static final Pattern OUT = Pattern.compile("(pos|neg)_(\\d+)_out\\.txt");
    private static final Pattern ARGS = Pattern.compile("(pos|neg)_(\\d+)_args\\.txt");

    public static String readInputStream(InputStream stream, Charset charset) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        String ret = reader.lines().reduce("", (left, right) -> left + right + "\n");
        reader.close();
        return ret;
    }

    public static void writeOutputStream(OutputStream stream, String body, Charset charset) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, charset));
        writer.write(body);
        writer.flush();
        writer.close();
    }

    public static String readResourceFile(String name) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream stream = classLoader.getResourceAsStream(name);
        if (stream == null) {
            return "";
        }
        return readInputStream(stream, StandardCharsets.UTF_8);
    }

    public static String readFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        return readInputStream(inputStream, StandardCharsets.UTF_8);
    }

    public static void writeFile(File file, String body) throws IOException {
        File parent = file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Can't create file" + file);
        }
        OutputStream stream = new FileOutputStream(file, false);
        writeOutputStream(stream, body, StandardCharsets.UTF_8);
    }

    public static String readMultiString(String stopCode) {
        Scanner in = new Scanner(System.in);
        StringBuilder ret = new StringBuilder();
        String temp = "";
        while (true) {
            temp = in.nextLine();
            if (temp.equals(stopCode)) {
                break;
            }
            ret.append(temp).append('\n');
        }
        return ret.toString();
    }

    public static Map<String, File> findAllFiles(File directory, Pattern pattern) {
        Objects.requireNonNull(directory);
        Objects.requireNonNull(pattern);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalStateException("Invalid directory");
        }
        Map<String, File> ret = new HashMap<>();
        File[] found = directory.listFiles((file) -> file.isFile() && pattern.matcher(file.getName()).matches());
        if (found != null) {
            for (File file : found) {
                ret.put(file.getName(), file);
            }
        }
        return Collections.unmodifiableMap(ret);
    }

    private static MarkedTest processIn(File in) throws InvalidTestException {
        Matcher matcher = IN.matcher(in.getName());
        if (!matcher.matches()) {
            throw new InvalidTestException("Broken in file");
        }
        TestType type = TestType.fromName(matcher.group(1));
        int number = Integer.parseInt(matcher.group(2));
        MarkedTest ret = new MarkedTest();
        ret.setInput(in);
        ret.setNumber(number);
        ret.setType(type);
        return ret;
    }

    private static File findTestFile(Map<String, File> files, TestType type, int number, String tail) {
        String toFind = type.getName() + "_" + number + "_" + tail;
        File ret = files.get(toFind);
        if (ret != null) {
            return ret;
        }
        Pattern pattern = Pattern.compile(type.getName() + "_0+" + number + "_" + tail);
        for (File file : files.values()) {
            if (pattern.matcher(file.getName()).matches()) {
                return file;
            }
        }
        return null;
    }

    public static List<MarkedTest> findAllTests(File directory) throws InvalidTestException {
        Map<String, File> in = findAllFiles(directory, IN);
        Map<String, File> out = findAllFiles(directory, OUT);
        Map<String, File> args = findAllFiles(directory, ARGS);
        List<MarkedTest> ret = new ArrayList<>();
        for (File fileIn : in.values()) {
            MarkedTest test = processIn(fileIn);
            File outFile = findTestFile(out, test.getType(), test.getNumber(), "out.txt");
            if (outFile != null) {
                test.setOutput(outFile);
            } else if (test.getType() == TestType.POSITIVE) {
                throw new InvalidTestException(test);
            }
            File argsFile = findTestFile(args, test.getType(), test.getNumber(), "args.txt");
            if (argsFile != null) {
                test.setArguments(argsFile);
            }
            ret.add(test);
        }
        return ret;
    }
}
