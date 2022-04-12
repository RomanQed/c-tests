package com.github.romanqed.ctests.mongo.serializers;

import com.github.romanqed.ctests.entities.Test;
import com.github.romanqed.ctests.tests.TestType;
import org.bson.Document;

import java.util.UUID;

public class TestSerializer implements MongoSerializer<Test> {
    @Override
    public Document serialize(Test object) {
        Document ret = new Document();
        ret.put("_id", object.getUUID().toString());
        ret.put("type", object.getType().toString());
        ret.put("lab", object.getLab());
        ret.put("task", object.getTask());
        ret.put("variant", object.getVariant());
        ret.put("input", object.getInput());
        if (object.getOutput() != null) {
            ret.put("output", object.getOutput());
        }
        if (object.getArguments() != null) {
            ret.put("arguments", object.getArguments());
        }
        return ret;
    }

    @Override
    public Test deserialize(Document source) {
        Test ret = new Test(UUID.fromString(source.getString("_id")));
        ret.setType(TestType.valueOf(source.getString("type")));
        ret.setLab(source.getInteger("lab"));
        ret.setTask(source.getInteger("task"));
        ret.setVariant(source.getInteger("variant"));
        ret.setInput(source.getString("input"));
        ret.setOutput(source.getString("output"));
        ret.setArguments(source.getString("arguments"));
        return ret;
    }
}
