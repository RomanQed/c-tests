package com.github.romanqed.ctests.storage;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    private final Map<String, Object> fields;

    public Storage() {
        fields = new ConcurrentHashMap<>();
    }

    public <T> void set(Field<T> field, T value) {
        Objects.requireNonNull(field);
        Objects.requireNonNull(value);
        fields.put(field.getName(), value);
    }

    public <T> void remove(Field<T> field) {
        Objects.requireNonNull(field);
        fields.remove(field.getName());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Field<T> field) {
        Objects.requireNonNull(field);
        return (T) fields.get(field.getName());
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
