package com.github.romanqed.ctests.mongo;

import com.github.romanqed.ctests.base.Entity;
import com.github.romanqed.ctests.mongo.serializers.MongoSerializer;
import com.github.romanqed.jutils.concurrent.SimpleTaskFabric;
import com.github.romanqed.jutils.concurrent.TaskFabric;
import com.github.romanqed.jutils.util.Checks;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Objects;

public class MongoRepositoryInstance {
    private final MongoDatabase database;
    private TaskFabric fabric;

    public MongoRepositoryInstance(MongoDatabase database, TaskFabric fabric) {
        this.database = Objects.requireNonNull(database);
        this.fabric = Checks.requireNonNullElse(fabric, new SimpleTaskFabric());
    }

    public MongoRepositoryInstance(MongoDatabase database) {
        this(database, null);
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public TaskFabric getFabric() {
        return fabric;
    }

    public void setFabric(TaskFabric fabric) {
        this.fabric = Objects.requireNonNull(fabric);
    }

    public <E extends Entity> MongoAdapter<E> createAdapter(String collection, MongoSerializer<E> serializer) {
        MongoCollection<Document> ret = database.getCollection(Objects.requireNonNull(collection));
        return new MongoAdapter<>(fabric, ret, serializer);
    }
}
