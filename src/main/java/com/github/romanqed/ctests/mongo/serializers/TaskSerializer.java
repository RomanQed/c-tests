package com.github.romanqed.ctests.mongo.serializers;

import com.github.romanqed.ctests.entities.Task;
import org.bson.Document;

import java.util.UUID;

public class TaskSerializer implements MongoSerializer<Task> {
    @Override
    public Document serialize(Task object) {
        Document ret = new Document();
        ret.put("_id", object.getUUID().toString());
        ret.put("number", object.getNumber());
        ret.put("lab", object.getLab());
        return ret;
    }

    @Override
    public Task deserialize(Document source) {
        Task ret = new Task(UUID.fromString(source.getString("_id")));
        ret.setLab(source.getInteger("lab"));
        ret.setNumber(source.getInteger("number"));
        return ret;
    }
}
