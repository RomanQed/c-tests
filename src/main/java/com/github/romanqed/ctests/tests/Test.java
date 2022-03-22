package com.github.romanqed.ctests.tests;

import java.io.File;
import java.util.Objects;

public class Test {
    private File input;
    private File output;
    private File arguments;

    public File getInput() {
        return input;
    }

    public void setInput(File input) {
        Objects.requireNonNull(input);
        this.input = input;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        Objects.requireNonNull(input);
        this.output = output;
    }

    public File getArguments() {
        return arguments;
    }

    public void setArguments(File arguments) {
        Objects.requireNonNull(input);
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "In: " + input + " Out: " + output + " Args: " + arguments;
    }
}
