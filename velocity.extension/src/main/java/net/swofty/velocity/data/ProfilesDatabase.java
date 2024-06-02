package net.swofty.velocity.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ProfilesDatabase(String id) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    @Override
    public MongoDB connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("data");
        return this;
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
        Document doc = collection.find(Filters.eq("ignLowercase", username.toLowerCase())).first();
        if (doc == null)
            return null;
        return UUID.fromString(doc.getString("_id"));
    }

    public static Document fetchDocument(String uniqueId) {
        return collection.find(Filters.eq("_id", uniqueId)).first();
    }
}

