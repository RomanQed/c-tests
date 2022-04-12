package com.github.romanqed.ctests.mongo;

import com.github.romanqed.ctests.entities.Lab;
import com.github.romanqed.ctests.entities.Task;
import com.github.romanqed.ctests.entities.Test;
import com.github.romanqed.ctests.entities.Variant;
import com.github.romanqed.ctests.mongo.serializers.LabSerializer;
import com.github.romanqed.ctests.mongo.serializers.TaskSerializer;
import com.github.romanqed.ctests.mongo.serializers.TestSerializer;
import com.github.romanqed.ctests.mongo.serializers.VariantSerializer;

public class Store {
    public static final MongoAdapter<Lab> LABS;
    public static final MongoAdapter<Task> TASKS;
    public static final MongoAdapter<Variant> VARIANTS;
    public static final MongoAdapter<Test> TESTS;

    static {
        LABS = MongoRepository.createAdapter("labs", new LabSerializer());
        TASKS = MongoRepository.createAdapter("tasks", new TaskSerializer());
        VARIANTS = MongoRepository.createAdapter("variants", new VariantSerializer());
        TESTS = MongoRepository.createAdapter("tests", new TestSerializer());
    }
}
