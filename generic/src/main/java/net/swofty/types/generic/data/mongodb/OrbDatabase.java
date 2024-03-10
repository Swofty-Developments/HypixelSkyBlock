package net.swofty.types.generic.data.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.item.ItemType;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class OrbDatabase {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("orbs");
    }

    public static Document getDocument(String key) {
        return collection.find(Filters.eq("_id", key)).first();
    }

    public void addOrb(String url, Pos position, ItemType itemType) {
        Document doc = new Document();
        doc.append("_id", collection.countDocuments() + 1);
        doc.append("url", url);
        doc.append("x", position.x());
        doc.append("y", position.y());
        doc.append("z", position.z());
        doc.append("itemType", itemType.name());
        collection.insertOne(doc);
    }

    public void removeOrbs(Pos position, double distance) {
        for (Document doc : collection.find()) {
            int x = doc.getInteger("x");
            int y = doc.getInteger("y");
            int z = doc.getInteger("z");
            if (position.distance(new Pos(x, y, z)) <= distance) {
                collection.deleteOne(Filters.eq("_id", doc.getInteger("_id")));
            }
        }
    }

    public static List<OrbData> getAllOrbs() {
        List<OrbData> orbs = new ArrayList<>();
        for (Document doc : collection.find()) {
            long id = doc.getLong("_id");
            String url = doc.getString("url");
            Double x = doc.getDouble("x");
            Double y = doc.getDouble("y");
            Double z = doc.getDouble("z");
            ItemType itemType = ItemType.valueOf(doc.getString("itemType"));
            OrbData orb = new OrbData();

            orb.url = url;
            orb.position = new Pos(x + 0.5, y, z + 0.5);
            orb.itemType = itemType;

            orbs.add(orb);
        }
        return orbs;
    }

    public static class OrbData {
        public String url;
        public Pos position;
        public ItemType itemType;
    }
}
