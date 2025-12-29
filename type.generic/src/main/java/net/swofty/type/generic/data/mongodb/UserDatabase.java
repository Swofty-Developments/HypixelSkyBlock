package net.swofty.type.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.DataHandler;
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
        if (document == null || !document.containsKey("profiles")) {
            return new SkyBlockPlayerProfiles(id);
        }
        return SkyBlockPlayerProfiles.deserialize(document);
    }

    public void deleteSelf() {
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
    public void saveData(DataHandler handler) {
        Document userDoc;
        boolean exists = collection.find(new Document("_id", id.toString())).first() != null;

        if (exists) {
            userDoc = collection.find(new Document("_id", id.toString())).first();
        } else {
            userDoc = new Document("_id", id.toString());
        }

        // Add the handler's document data to the user document, only updating fields from this handler
        Document handlerDoc = handler.toDocument();
        Document updateFields = new Document();

        for (String key : handlerDoc.keySet()) {
            if (!key.equals("_owner")) { // Skip the _owner field to avoid conflicts
                updateFields.put(key, handlerDoc.get(key));
            }
        }

        if (exists) {
            // Use $set to only update specific fields, never removing existing ones
            collection.updateOne(
                    new Document("_id", id.toString()),
                    new Document("$set", updateFields)
            );
        } else {
            userDoc.putAll(updateFields);
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