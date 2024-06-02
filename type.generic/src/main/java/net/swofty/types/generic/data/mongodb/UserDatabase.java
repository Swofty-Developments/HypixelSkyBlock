package net.swofty.types.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.swofty.types.generic.user.PlayerProfiles;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

public class UserDatabase {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public UUID id;

    public UserDatabase(UUID id) {
        this.id = id;
    }

    public UserDatabase(String id) {
        this.id = UUID.fromString(id);
    }

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("profiles");
    }

    public PlayerProfiles getProfiles() {
        Document document = collection.find(new Document("_id", id.toString())).first();
        if (document == null) {
            return new PlayerProfiles(id);
        }
        return PlayerProfiles.deserialize(document);
    }

    public void deleteProfiles() {
        collection.deleteOne(new Document("_id", id.toString()));
    }

    public void saveProfiles(PlayerProfiles profiles) {
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
