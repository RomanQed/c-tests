package com.github.romanqed.ctests.base;

import com.github.romanqed.jutils.util.Checks;

import java.util.UUID;

public abstract class AbstractEntity implements Entity {
    private final UUID uuid;

    public AbstractEntity(UUID uuid) {
        this.uuid = Checks.requireNonNullElse(uuid, UUID.randomUUID());
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
