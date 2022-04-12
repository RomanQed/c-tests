package com.github.romanqed.ctests.base;

public interface EntitySerializer<E extends Entity, D> {
    D serialize(E object);

    E deserialize(D source);
}
