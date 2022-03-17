package com.github.romanqed.ctests.storage;

import java.util.Objects;

public class StorageProvider {
    private static Storage storage = new Storage();

    public static Storage getStorage() {
        return storage;
    }

    public synchronized static void setStorage(Storage storage) {
        StorageProvider.storage = Objects.requireNonNull(storage);
    }
}
