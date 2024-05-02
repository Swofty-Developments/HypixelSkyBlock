package net.swofty.types.generic.data.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ProfilesDatabase(String id) implements MongoDB {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("data");
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    public static void replaceDocument(String uniqueId, Document document) {
        collection.replaceOne(Filters.eq("_id", uniqueId), document);
    }

    public List<Document> getAll() {
        FindIterable<Document> results = collection.find();
        List<Document> list = new ArrayList<>();
        for (Document doc : results) {
            list.add(doc);
        }
        return list;
    }

    public Document getDocument() {
        Document query = new Document("_id", id);
        return collection.find(query).first();
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = collection.find(query).first();

        if (found == null) {
            return false;
        }

        collection.deleteOne(query);
        return true;
    }

    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", id);
            Document found = collection.find(query).first();

            assert found != null;
            collection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document New = new Document("_id", id);
        New.append(key, value);
        collection.insertOne(New);
    }

    public boolean exists() {
        Document query = new Document("_id", id);
        Document found = collection.find(query).first();
        return found != null;
    }

    public static UUID fetchUUID(String username) {
        Document doc = collection.find(Filters.eq("ignLowercase", "\"" + username.toLowerCase() + "\"")).first();
        if (doc == null)
            return null;
        return UUID.fromString(doc.getString("_owner"));
    }

    public static Document fetchDocument(String uniqueId) {
        return collection.find(Filters.eq("_id", uniqueId)).first();
    }

    public static Document fetchDocument(UUID uniqueId) {
        return collection.find(Filters.eq("_id", uniqueId.toString())).first();
    }

    public static void deleteDocument(String uniqueId) {
        collection.deleteOne(Filters.eq("_id", uniqueId));
    }
}
