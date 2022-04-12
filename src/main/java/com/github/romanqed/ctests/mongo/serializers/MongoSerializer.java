package com.github.romanqed.ctests.mongo.serializers;

import com.github.romanqed.ctests.base.Entity;
import com.github.romanqed.ctests.base.EntitySerializer;
import org.bson.Document;

public interface MongoSerializer<E extends Entity> extends EntitySerializer<E, Document> {
}
