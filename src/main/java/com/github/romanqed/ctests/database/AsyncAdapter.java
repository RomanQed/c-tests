package com.github.romanqed.ctests.database;

import com.github.romanqed.ctests.base.Entity;
import com.github.romanqed.jutils.concurrent.Task;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AsyncAdapter<E extends Entity, F> extends DatabaseAdapter<E, F> {
    Task<Long> asyncPut(E entity);

    Task<Long> asyncPut(Collection<E> entities);

    Task<E> asyncFind(UUID uuid);

    Task<List<E>> asyncFindAll(Collection<UUID> uuids);

    Task<List<E>> asyncFind(F filter);

    Task<E> asyncFindFirst(F filter);

    Task<Long> asyncCount(F filter);

    Task<Long> asyncDelete(F filter);

    Task<Long> asyncDeleteAll(F filter);

    Task<Long> asyncDeleteAll(String field, Collection<?> values);

    Task<Long> asyncDelete(UUID uuid);

    Task<Long> asyncDeleteAll(Collection<UUID> uuids);
}
