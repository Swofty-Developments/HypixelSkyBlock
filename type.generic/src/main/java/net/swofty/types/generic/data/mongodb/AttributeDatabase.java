package net.swofty.types.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class AttributeDatabase {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("attribute");
    }

    public static void saveDocument(Document document) {
        if (collection.find(Filters.eq("_id", document.getString("_id"))).first() != null) {
            collection.replaceOne(Filters.eq("_id", document.getString("_id")), document);
        } else {
            collection.insertOne(document);
        }
    }

    public static Document getDocument(String key) {
        return collection.find(Filters.eq("_id", key)).first();
    }
}
