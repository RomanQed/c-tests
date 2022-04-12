package com.github.romanqed.ctests.mongo;

import com.github.romanqed.ctests.base.Entity;
import com.github.romanqed.ctests.database.AbstractAsyncAdapter;
import com.github.romanqed.ctests.mongo.serializers.MongoSerializer;
import com.github.romanqed.jutils.concurrent.Task;
import com.github.romanqed.jutils.concurrent.TaskFabric;
import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class MongoAdapter<E extends Entity> extends AbstractAsyncAdapter<E, Bson> {
    private final MongoCollection<Document> collection;
    private final MongoSerializer<E> serializer;

    public MongoAdapter(TaskFabric fabric, MongoCollection<Document> collection, MongoSerializer<E> serializer) {
        super(fabric);
        this.collection = Objects.requireNonNull(collection);
        this.serializer = Objects.requireNonNull(serializer);
    }

    public MongoAdapter(TaskFabric fabric, MongoCollection<Document> collection) {
        this(fabric, collection, null);
    }

    @Override
    public long put(E entity) {
        Document toPut = serializer.serialize(entity);
        try {
            if (collection.insertOne(toPut).wasAcknowledged()) {
                return 1;
            }
            return 0;
        } catch (MongoWriteException e) {
            if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY) {
                collection.replaceOne(eq("_id", toPut.get("_id")), toPut);
            } else {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @Override
    public long put(Collection<E> entities) {
        long ret = 0;
        for (E entity : entities) {
            ret += put(entity);
        }
        return ret;
    }

    @Override
    public E find(UUID uuid) {
        return findFirst(eq(uuid.toString()));
    }

    @Override
    public List<E> findAll(Collection<UUID> uuids) {
        Bson filter = empty();
        for (UUID uuid : uuids) {
            filter = or(filter, eq("_id", uuid.toString()));
        }
        Iterable<Document> found = collection.find(filter);
        List<E> ret = new ArrayList<>();
        for (Document item : found) {
            ret.add(serializer.deserialize(item));
        }
        return ret;
    }

    @Override
    public List<E> find(Bson filter) {
        FindIterable<Document> found;
        try {
            found = collection.find(filter);
        } catch (Exception e) {
            return null;
        }
        List<E> ret = new ArrayList<>();
        found.iterator().forEachRemaining(document -> ret.add(serializer.deserialize(document)));
        return ret;
    }

    public List<E> find(String key, Object value) {
        return find(eq(key, value));
    }

    public Task<List<E>> asyncFind(String key, Object value) {
        return super.asyncFind(eq(key, value));
    }

    @Override
    public E findFirst(Bson filter) {
        try {
            Document found = Objects.requireNonNull(collection.find(filter).first());
            return serializer.deserialize(found);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long count(Bson filter) {
        return collection.countDocuments(filter);
    }

    public long count(String key, Object value) {
        return count(eq(key, value));
    }

    public Task<Long> asyncCount(String key, Object value) {
        return super.asyncCount(eq(key, value));
    }

    public E findFirst(String key, Object value) {
        return findFirst(eq(key, value));
    }

    public Task<E> asyncFindFirst(String key, Object value) {
        return super.asyncFindFirst(eq(key, value));
    }

    @Override
    public long delete(Bson filter) {
        try {
            return collection.deleteOne(filter).getDeletedCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public long delete(String key, Object value) {
        return delete(eq(key, value));
    }

    public Task<Long> asyncDelete(String key, Object value) {
        return super.asyncDelete(eq(key, value));
    }

    @Override
    public long deleteAll(Bson filter) {
        try {
            return collection.deleteMany(filter).getDeletedCount();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long deleteAll(String field, Collection<?> values) {
        Bson filter = Filters.or(values.stream().map(value -> Filters.eq(field, value)).collect(Collectors.toList()));
        return deleteAll(filter);
    }

    public long deleteAll(String key, Object value) {
        return deleteAll(eq(key, value));
    }

    public Task<Long> asyncDeleteAll(String key, Object value) {
        return super.asyncDeleteAll(eq(key, value));
    }

    @Override
    public long delete(UUID uuid) {
        return delete(eq(uuid.toString()));
    }

    @Override
    public long deleteAll(Collection<UUID> uuids) {
        List<String> converted = uuids.stream().map(UUID::toString).collect(Collectors.toList());
        return deleteAll("_id", converted);
    }
}
