package com.github.romanqed.ctests.mongo.serializers;

import com.github.romanqed.ctests.entities.Lab;
import org.bson.Document;

import java.util.UUID;

public class LabSerializer implements MongoSerializer<Lab> {
    @Override
    public Document serialize(Lab object) {
        Document ret = new Document();
        ret.put("_id", object.getUUID().toString());
        ret.put("number", object.getNumber());
        return ret;
    }

    @Override
    public Lab deserialize(Document source) {
        Lab ret = new Lab(UUID.fromString(source.getString("_id")));
        ret.setNumber(source.getInteger("number"));
        return ret;
    }
}
