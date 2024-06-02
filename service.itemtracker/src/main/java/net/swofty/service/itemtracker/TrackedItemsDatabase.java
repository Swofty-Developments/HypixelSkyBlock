package net.swofty.service.itemtracker;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import net.swofty.commons.TrackedItem;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record TrackedItemsDatabase(UUID itemUUID) {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("tracked-items");
    }

    public static Integer getNumberMade(String itemType) {
        return (int) collection.countDocuments(Filters.eq("item-type", itemType));
    }

    public boolean exists() {
        return collection.find(Filters.eq("_id", itemUUID.toString())).first() != null;
    }

    public TrackedItem get() {
        Document document = collection.find(Filters.eq("_id", itemUUID.toString())).first();
        return document == null ? null : TrackedItem.fromDocument(document);
    }

    public void insertOrUpdate(TrackedItem trackedItem) {
        if (!exists()) {
            collection.insertOne(trackedItem.toDocument().append("_id", trackedItem.itemUUID.toString()));
            return;
        }
        collection.replaceOne(Filters.eq("_id", trackedItem.itemUUID.toString()), trackedItem.toDocument());
    }
}

