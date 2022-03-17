package com.github.romanqed.ctests.tests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum TestType {
    NEGATIVE("neg"),
    POSITIVE("pos");

    private static final Map<String, TestType> body = toMap();

    final String name;

    TestType(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public static TestType fromName(String name) {
        Objects.requireNonNull(name);
        return Objects.requireNonNull(body.get(name));
    }

    private static Map<String, TestType> toMap() {
        Map<String, TestType> ret = new HashMap<>();
        for (TestType type : TestType.values()) {
            ret.put(type.name, type);
        }
        return Collections.unmodifiableMap(ret);
    }

    public String getName() {
        return name;
    }
}
