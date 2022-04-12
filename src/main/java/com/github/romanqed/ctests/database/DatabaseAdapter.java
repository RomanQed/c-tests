package com.github.romanqed.ctests.database;

import com.github.romanqed.ctests.base.Entity;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DatabaseAdapter<E extends Entity, F> {
    long put(E entity);

    long put(Collection<E> entities);

    E find(UUID uuid);

    List<E> findAll(Collection<UUID> uuids);

    List<E> find(F filter);

    E findFirst(F filter);

    long count(F filter);

    long delete(F filter);

    long deleteAll(F filter);

    long deleteAll(String field, Collection<?> values);

    long delete(UUID uuid);

    long deleteAll(Collection<UUID> uuids);
}
