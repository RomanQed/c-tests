package com.github.romanqed.ctests.macro;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum MacroType {
    COMMAND('/'),
    SUBSTITUTION('%'),
    DECLARE('!');

    private static final Map<Character, MacroType> body = toMap();
    private final char key;

    MacroType(char key) {
        this.key = key;
    }

    private static Map<Character, MacroType> toMap() {
        Map<Character, MacroType> ret = new HashMap<>();
        for (MacroType type : MacroType.values()) {
            ret.put(type.key, type);
        }
        return Collections.unmodifiableMap(ret);
    }

    public static MacroType fromKey(char key) {
        return Objects.requireNonNull(body.get(key));
    }
}
