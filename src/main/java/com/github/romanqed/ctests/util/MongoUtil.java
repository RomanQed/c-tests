package com.github.romanqed.ctests.util;

import com.github.romanqed.ctests.entities.Lab;
import com.github.romanqed.ctests.entities.Task;
import com.github.romanqed.ctests.entities.Variant;
import com.github.romanqed.ctests.mongo.Store;
import com.github.romanqed.ctests.tasks.TaskData;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.Objects;

public class MongoUtil {
    public static boolean putTaskData(TaskData data) {
        Objects.requireNonNull(data);
        long count = 0;
        Bson filter = Filters.eq("number", data.getLabNumber());
        if (Store.LABS.find(filter).isEmpty()) {
            Lab toPut = new Lab();
            toPut.setNumber(data.getLabNumber());
            count += Store.LABS.put(toPut);
        }
        filter = Filters.and(Filters.eq("number", data.getNumber()),
                Filters.eq("lab", data.getLabNumber()));
        if (Store.TASKS.find(filter).isEmpty()) {
            Task toPut = new Task();
            toPut.setLab(data.getLabNumber());
            toPut.setNumber(data.getNumber());
            count += Store.TASKS.put(toPut);
        }
        filter = Filters.and(Filters.eq("lab", data.getLabNumber()),
                Filters.eq("task", data.getNumber()),
                Filters.eq("variant", data.getVariant()));
        if (Store.VARIANTS.find(filter).isEmpty()) {
            Variant toPut = new Variant();
            toPut.setLab(data.getLabNumber());
            toPut.setTask(data.getNumber());
            toPut.setNumber(data.getVariant());
            count += Store.VARIANTS.put(toPut);
        }
        return count != 0;
    }
}
