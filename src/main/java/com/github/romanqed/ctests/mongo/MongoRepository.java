package com.github.romanqed.ctests.mongo;

import com.github.romanqed.ctests.base.Entity;
import com.github.romanqed.ctests.mongo.serializers.MongoSerializer;
import com.github.romanqed.jutils.concurrent.SimpleTaskFabric;
import com.github.romanqed.jutils.concurrent.TaskFabric;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import java.util.Objects;

public class MongoRepository {
    private static MongoRepositoryInstance primaryInstance;
    private static TaskFabric fabric = new SimpleTaskFabric();

    public static void init(MongoClient client, String database) {
        primaryInstance = new MongoRepositoryInstance(client.getDatabase(database), fabric);
    }

    public static MongoRepositoryInstance getPrimaryInstance() {
        return primaryInstance;
    }

    public static TaskFabric getFabric() {
        return fabric;
    }

    public static void setFabric(TaskFabric fabric) {
        MongoRepository.fabric = fabric;
        if (primaryInstance != null) {
            primaryInstance.setFabric(fabric);
        }
    }

    public static MongoDatabase getDatabase() {
        Objects.requireNonNull(primaryInstance);
        return primaryInstance.getDatabase();
    }

    public static <E extends Entity> MongoAdapter<E> createAdapter(String collection, MongoSerializer<E> serializer) {
        Objects.requireNonNull(primaryInstance);
        return primaryInstance.createAdapter(collection, serializer);
    }
}
