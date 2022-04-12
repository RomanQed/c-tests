package com.github.romanqed.ctests.database;

import com.github.romanqed.ctests.base.Entity;
import com.github.romanqed.jutils.concurrent.Task;
import com.github.romanqed.jutils.concurrent.TaskFabric;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractAsyncAdapter<E extends Entity, F> implements AsyncAdapter<E, F> {
    private final TaskFabric fabric;

    public AbstractAsyncAdapter(TaskFabric fabric) {
        this.fabric = Objects.requireNonNull(fabric);
    }

    @Override
    public Task<Long> asyncPut(E entity) {
        return fabric.createTask(() -> put(entity));
    }

    @Override
    public Task<Long> asyncPut(Collection<E> entities) {
        return fabric.createTask(() -> put(entities));
    }

    @Override
    public Task<E> asyncFind(UUID uuid) {
        return fabric.createTask(() -> find(uuid));
    }

    @Override
    public Task<List<E>> asyncFindAll(Collection<UUID> uuids) {
        return fabric.createTask(() -> findAll(uuids));
    }

    @Override
    public Task<List<E>> asyncFind(F filter) {
        return fabric.createTask(() -> find(filter));
    }

    @Override
    public Task<E> asyncFindFirst(F filter) {
        return fabric.createTask(() -> findFirst(filter));
    }

    @Override
    public Task<Long> asyncCount(F filter) {
        return fabric.createTask(() -> count(filter));
    }

    @Override
    public Task<Long> asyncDelete(F filter) {
        return fabric.createTask(() -> delete(filter));
    }

    @Override
    public Task<Long> asyncDeleteAll(F filter) {
        return fabric.createTask(() -> deleteAll(filter));
    }

    @Override
    public Task<Long> asyncDeleteAll(String field, Collection<?> values) {
        return fabric.createTask(() -> deleteAll(field, values));
    }

    @Override
    public Task<Long> asyncDelete(UUID uuid) {
        return fabric.createTask(() -> delete(uuid));
    }

    @Override
    public Task<Long> asyncDeleteAll(Collection<UUID> uuids) {
        return fabric.createTask(() -> deleteAll(uuids));
    }
}
