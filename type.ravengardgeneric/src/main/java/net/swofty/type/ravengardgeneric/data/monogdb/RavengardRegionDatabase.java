package net.swofty.type.ravengardgeneric.data.monogdb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.data.mongodb.MongoDB;
import net.swofty.type.ravengardgeneric.region.RavengardRegion;
import net.swofty.type.ravengardgeneric.region.RavengardRegionType;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public record RavengardRegionDatabase(String id) implements MongoDB {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("ravengard_regions");
    }

    public static List<RavengardRegion> getAllRegions() {
        List<RavengardRegion> regions = new ArrayList<>();
        if (collection == null) {
            return regions;
        }
        for (Document doc : collection.find()) {
            String name = doc.getString("_id");
            RavengardRegionType type = RavengardRegionType.fromKey(doc.getString("type"));
            if (type == null) {
                continue;
            }
            regions.add(new RavengardRegion(
                    name,
                    new Pos(doc.getInteger("x1"), doc.getInteger("y1"), doc.getInteger("z1")),
                    new Pos(doc.getInteger("x2"), doc.getInteger("y2"), doc.getInteger("z2")),
                    type,
                    ServerType.valueOf(doc.getOrDefault("serverType", ServerType.RAVENGARD_LOBBY.name()).toString())));
        }
        return regions;
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc == null ? def : doc.get(key);
    }

    public List<Document> getAll() {
        FindIterable<Document> results = collection.find();
        List<Document> list = new ArrayList<>();
        for (Document doc : results) {
            list.add(doc);
        }
        return list;
    }

    public Document getDocument() {
        return collection.find(new Document("_id", id)).first();
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        if (collection.find(query).first() == null) {
            return false;
        }
        collection.deleteOne(query);
        return true;
    }

    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document found = collection.find(new Document("_id", id)).first();
            assert found != null;
            collection.updateOne(found, Updates.set(key, value));
            return;
        }
        collection.insertOne(new Document("_id", id).append(key, value));
    }

    public boolean exists() {
        return collection.find(new Document("_id", id)).first() != null;
    }
}
