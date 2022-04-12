package com.github.romanqed.ctests.mongo.serializers;

import com.github.romanqed.ctests.entities.Variant;
import org.bson.Document;

import java.util.UUID;

public class VariantSerializer implements MongoSerializer<Variant> {
    @Override
    public Document serialize(Variant object) {
        Document ret = new Document();
        ret.put("_id", object.getUUID().toString());
        ret.put("number", object.getNumber());
        ret.put("task", object.getTask());
        ret.put("lab", object.getLab());
        return ret;
    }

    @Override
    public Variant deserialize(Document source) {
        Variant ret = new Variant(UUID.fromString(source.getString("_id")));
        ret.setNumber(source.getInteger("number"));
        ret.setTask(source.getInteger("task"));
        ret.setLab(source.getInteger("lab"));
        return ret;
    }
}
