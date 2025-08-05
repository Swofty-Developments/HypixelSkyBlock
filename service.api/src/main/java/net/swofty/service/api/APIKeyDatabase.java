package net.swofty.service.api;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record APIKeyDatabase(String key) {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("api-key");
    }

    public @Nullable APIKeyDatabaseObject fetch() {
        Document document = collection.find(new Document("_id", key)).first();
        if (document == null) {
            return null;
        }
        return APIKeyDatabaseObject.fromDocument(document);
    }

    public void delete() {
        collection.deleteOne(new Document("_id", key));
    }

    public static void insert(APIKeyDatabaseObject object) {
        collection.insertOne(object.toDocument());
    }

    public static List<APIKeyDatabaseObject> getAll() {
        FindIterable<Document> results = collection.find();
        List<Document> list = new ArrayList<>();
        for (Document doc : results) {
            list.add(doc);
        }
        return list.stream().map(APIKeyDatabaseObject::fromDocument).toList();
    }
}
