package net.swofty.type.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import net.swofty.PlayerField;
import net.swofty.codec.Codecs;
import net.swofty.commons.data.SwoftyData;
import net.swofty.commons.skyblock.SkyBlockPlayerProfiles;
import net.swofty.type.generic.data.DataHandler;
import net.swofty.type.generic.data.HypixelDataHandler;
import org.bson.Document;

import java.util.UUID;
import java.util.stream.Collectors;

public class UserDatabase {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    private static final PlayerField<String> PROFILES_INDEX =
            PlayerField.create("hypixel", "_profiles_index", Codecs.STRING, null);

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
        String stored = SwoftyData.account().get(id, PROFILES_INDEX);
        if (stored == null || stored.isEmpty()) {
            return new SkyBlockPlayerProfiles(id);
        }
        SkyBlockPlayerProfiles profiles = new SkyBlockPlayerProfiles(id);
        String[] parts = stored.split(";", 2);
        if (!parts[0].isEmpty()) profiles.setCurrentlySelected(UUID.fromString(parts[0]));
        if (parts.length > 1 && !parts[1].isEmpty()) {
            for (String profile : parts[1].split(",")) profiles.addProfile(UUID.fromString(profile));
        }
        return profiles;
    }

    public void deleteSelf() {
        SwoftyData.account().set(id, PROFILES_INDEX, null);
    }

    public void saveProfiles(SkyBlockPlayerProfiles profiles) {
        String selected = profiles.getCurrentlySelected() == null ? "" : profiles.getCurrentlySelected().toString();
        String list = profiles.getProfiles().stream().map(UUID::toString).collect(Collectors.joining(","));
        SwoftyData.account().set(id, PROFILES_INDEX, selected + ";" + list);
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