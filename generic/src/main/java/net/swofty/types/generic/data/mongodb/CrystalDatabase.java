package net.swofty.types.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.item.ItemType;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class CrystalDatabase {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("crystals");
    }

    public static Document getDocument(String key) {
        return collection.find(Filters.eq("_id", key)).first();
    }

    public static List<CrystalData> getFromAround(ServerType type, Pos pos, double distance) {
        List<CrystalData> crystals = new ArrayList<>();
        for (Document doc : collection.find()) {
            if (ServerType.valueOf(doc.getString("serverType")) != type) {
                continue;
            }
            double x = doc.getDouble("x");
            double y = doc.getDouble("y");
            double z = doc.getDouble("z");
            if (pos.distance(new Pos(x, y, z)) <= distance) {
                Integer id = doc.getInteger("_id");
                String url = doc.getString("url");
                Double x1 = doc.getDouble("x");
                Double y1 = doc.getDouble("y");
                Double z1 = doc.getDouble("z");
                ItemType itemType = ItemType.valueOf(doc.getString("itemType"));
                ServerType serverType = ServerType.valueOf(doc.getString("serverType"));

                CrystalData crystal = new CrystalData();

                crystal.url = url;
                crystal.position = new Pos(x1 + 0.5, y1, z1 + 0.5);
                crystal.itemType = itemType;
                crystal.serverType = serverType;

                crystals.add(crystal);
            }
        }
        return crystals;
    }

    public static void addCrystal(String url, Pos position, ItemType itemType) {
        Document doc = new Document();
        doc.append("_id", Math.toIntExact((collection.countDocuments() + 1)));
        doc.append("url", url);
        doc.append("x", position.x());
        doc.append("y", position.y());
        doc.append("z", position.z());
        doc.append("itemType", itemType.name());
        doc.append("serverType", SkyBlockConst.getTypeLoader().getType().name());
        collection.insertOne(doc);
    }

    public void removeCrystals(Pos position, double distance) {
        for (Document doc : collection.find()) {
            int x = doc.getInteger("x");
            int y = doc.getInteger("y");
            int z = doc.getInteger("z");
            if (position.distance(new Pos(x, y, z)) <= distance) {
                collection.deleteOne(Filters.eq("_id", doc.getInteger("_id")));
            }
        }
    }

    public static List<CrystalData> getAllCrystals() {
        List<CrystalData> crystals = new ArrayList<>();
        for (Document doc : collection.find()) {
            Integer id = doc.getInteger("_id");
            String url = doc.getString("url");
            Double x = doc.getDouble("x");
            Double y = doc.getDouble("y");
            Double z = doc.getDouble("z");
            // Manually handle DB migrations
            ItemType itemType;
            try {
                itemType = ItemType.valueOf(doc.getString("itemType"));
            } catch (Exception e) {
                Logger.error("Error parsing crystal data for crystal with ID " + id + " - skipping.");
                continue;
            }

            CrystalData crystal = new CrystalData();

            crystal.url = url;
            crystal.position = new Pos(x + 0.5, y, z + 0.5);
            crystal.itemType = itemType;
            crystal.serverType = ServerType.valueOf(doc.getString("serverType"));

            crystals.add(crystal);
        }
        return crystals;
    }

    public static class CrystalData {
        public String url;
        public Pos position;
        public ItemType itemType;
        public ServerType serverType;
    }
}
