package com.github.romanqed.ctests.tests;

import java.io.File;
import java.util.Objects;

public class Test {
    private File input;
    private File output;
    private File arguments;

    private void checkFile(File file) {
        Objects.requireNonNull(file);
        if (!(file.isFile() && file.exists())) {
            throw new IllegalStateException("File " + file + " is invalid");
        }
    }

    public File getInput() {
        return input;
    }

    public void setInput(File input) {
        checkFile(input);
        this.input = input;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        checkFile(output);
        this.output = output;
    }

    public File getArguments() {
        return arguments;
    }

    public void setArguments(File arguments) {
        checkFile(arguments);
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "In: " + input + " Out: " + output + " Args: " + arguments;
    }
}
