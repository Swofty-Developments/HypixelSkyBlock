package net.swofty.type.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.swofty.commons.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.HypixelDataHandler;
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

    public SkyBlockPlayerProfiles getProfiles() {
        Document document = collection.find(new Document("_id", id.toString())).first();
        if (document == null) {
            return new SkyBlockPlayerProfiles(id);
        }
        return SkyBlockPlayerProfiles.deserialize(document);
    }

    public void deleteProfiles() {
        collection.deleteOne(new Document("_id", id.toString()));
    }

    public void saveProfiles(SkyBlockPlayerProfiles profiles) {
        Map<String, Object> map = profiles.serialize();

        Document document = new Document("_id", id.toString());
        document.putAll(map);

        // Preserve existing Hypixel data when saving profiles
        Document existing = collection.find(new Document("_id", id.toString())).first();
        if (existing != null) {
            // Copy over Hypixel data fields
            for (HypixelDataHandler.Data data : HypixelDataHandler.Data.values()) {
                if (existing.containsKey(data.getKey())) {
                    document.put(data.getKey(), existing.get(data.getKey()));
                }
            }
            collection.replaceOne(new Document("_id", id.toString()), document);
        } else {
            collection.insertOne(document);
        }
    }

    /**
     * Saves Hypixel data to the user document (account-wide data)
     */
    public void saveHypixelData(HypixelDataHandler handler) {
        Document userDoc;
        boolean exists = collection.find(new Document("_id", id.toString())).first() != null;

        if (exists) {
            userDoc = collection.find(new Document("_id", id.toString())).first();
        } else {
            userDoc = new Document("_id", id.toString());
        }

        // Add all Hypixel data to the user document
        for (HypixelDataHandler.Data data : HypixelDataHandler.Data.values()) {
            try {
                userDoc.put(data.getKey(), handler.getDatapoint(data.getKey()).getSerializedValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (exists) {
            collection.replaceOne(new Document("_id", id.toString()), userDoc);
        } else {
            collection.insertOne(userDoc);
        }
    }

    /**
     * Loads Hypixel data from the user document
     */
    public Document getHypixelData() {
        return collection.find(new Document("_id", id.toString())).first();
    }

    /**
     * Updates a specific Hypixel data field
     */
    public void updateHypixelField(String key, Object value) {
        Document query = new Document("_id", id.toString());
        if (collection.find(query).first() != null) {
            collection.updateOne(query, Updates.set(key, value));
        } else {
            Document newDoc = new Document("_id", id.toString());
            newDoc.put(key, value);
            collection.insertOne(newDoc);
        }
    }

    public boolean exists() {
        Document query = new Document("_id", id.toString());
        Document found = collection.find(query).first();
        return found != null;
    }
}