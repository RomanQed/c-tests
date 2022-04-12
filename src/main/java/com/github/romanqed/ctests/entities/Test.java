package com.github.romanqed.ctests.entities;

import com.github.romanqed.ctests.base.AbstractEntity;
import com.github.romanqed.ctests.tests.TestType;
import com.github.romanqed.jutils.util.Checks;

import java.util.Objects;
import java.util.UUID;

public class Test extends AbstractEntity {
    private TestType type;
    private String input;
    private String output;
    private String arguments;
    private int lab;
    private int task;
    private int variant;

    public Test(UUID uuid) {
        super(uuid);
    }

    public Test() {
        super(null);
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = Objects.requireNonNull(type);
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = Objects.requireNonNull(input);
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public int getLab() {
        return lab;
    }

    public void setLab(int lab) {
        this.lab = Checks.requireCorrectValue(lab, e -> e > 0);
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = Checks.requireCorrectValue(task, e -> e > 0);
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }
}
