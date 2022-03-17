package com.github.romanqed.ctests.commands;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CommandCollection implements Collection<Command> {
    private final Map<String, Command> body;

    public CommandCollection(Collection<Command> commands) {
        this();
        Objects.requireNonNull(commands);
        commands.forEach(e -> body.put(e.name, e));
    }

    public CommandCollection() {
        body = new ConcurrentHashMap<>();
    }

    public Command get(String name) {
        return body.get(name);
    }

    @Override
    public int size() {
        return body.size();
    }

    @Override
    public boolean isEmpty() {
        return body.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Command)) {
            return false;
        }
        return body.containsValue(o);
    }

    @Override
    public Iterator<Command> iterator() {
        return body.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return body.values().toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        Command[] array = (Command[]) a;
        if (array.length < body.size()) {
            array = new Command[0];
        }
        return (T[]) body.values().toArray(array);
    }

    @Override
    public boolean add(Command command) {
        return body.put(command.name, command) == null;
    }

    @Override
    public boolean remove(Object o) {
        return body.values().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return body.values().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Command> c) {
        boolean ret = false;
        for (Command command : c) {
            ret = ret || add(command);
        }
        return ret;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return body.values().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return body.values().retainAll(c);
    }

    @Override
    public void clear() {
        body.clear();
    }
}
