package net.swofty.data.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.minestom.server.coordinate.Pos;
import net.swofty.region.RegionType;
import net.swofty.region.SkyBlockRegion;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class AttributeDatabase {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("attribute");
    }

    public static void saveDocument(Document document) {
        if (collection.find(Filters.eq("_id", document.getString("_id"))).first() != null) {
            collection.replaceOne(Filters.eq("_id", document.getString("_id")), document);
        } else {
            collection.insertOne(document);
        }
    }

    public static Document getDocument(String key) {
        return collection.find(Filters.eq("_id", key)).first();
    }
}
