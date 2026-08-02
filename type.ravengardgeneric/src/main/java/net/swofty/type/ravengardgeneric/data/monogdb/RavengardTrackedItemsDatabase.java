package net.swofty.type.ravengardgeneric.data.monogdb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.UUID;

/**
 * Records every tracked item the moment it is minted, mirroring SkyBlock's item tracker.
 * SkyBlock routes these through the item tracker service; Ravengard writes straight to its own
 * collection until it has enough traffic to justify one.
 */
public final class RavengardTrackedItemsDatabase {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    private RavengardTrackedItemsDatabase() {
    }

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("ravengard_tracked_items");
    }

    public static void record(UUID itemUuid, String itemId, UUID owner, UUID profile) {
        if (collection == null) {
            return;
        }
        Thread.startVirtualThread(() -> {
            Document document = new Document("_id", itemUuid.toString());
            document.put("item", itemId);
            document.put("owner", owner == null ? null : owner.toString());
            document.put("profile", profile == null ? null : profile.toString());
            document.put("created", System.currentTimeMillis());
            collection.insertOne(document);
        });
    }
}
