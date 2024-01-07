package net.swofty.commons.skyblock.data.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.swofty.commons.skyblock.user.UserProfiles;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

public class UserDatabase {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public UUID id;

    public UserDatabase(UUID id) {
        this.id = id;
    }

    public UserDatabase(String id) {
        this.id = UUID.fromString(id);
    }

    public static void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("profiles");
    }

    public UserProfiles getProfiles() {
        Document document = collection.find(new Document("_id", id.toString())).first();
        if (document == null) {
            return new UserProfiles(id);
        }
        return UserProfiles.deserialize(document);
    }

    public void saveProfiles(UserProfiles profiles) {
        Map<String, Object> map = profiles.serialize();

        Document document = new Document("_id", id.toString());
        document.putAll(map);

        if (collection.find(new Document("_id", id.toString())).first() != null) {
            collection.replaceOne(new Document("_id", id.toString()), document);
        } else {
            collection.insertOne(document);
        }
    }
}
