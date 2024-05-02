package net.swofty.types.generic.data.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.minestom.server.coordinate.Pos;
import net.swofty.types.generic.user.fairysouls.FairySoul;
import net.swofty.types.generic.user.fairysouls.FairySoulZone;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FairySoulDatabase {

    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("fairysouls");
    }

    public static Document getDocument(String key) {
        return collection.find(Filters.eq("_id", key)).first();
    }

    public static List<FairySoul> getAllSouls() {
        List<FairySoul> souls = new ArrayList<>();
        for (Document doc : collection.find()) {
            int id = doc.getInteger("_id");
            FairySoulZone zone = FairySoulZone.valueOf(doc.getString("zone"));
            int x = doc.getInteger("x");
            int y = doc.getInteger("y");
            int z = doc.getInteger("z");
            FairySoul soul = new FairySoul(
                    id,
                    new Pos(x + 0.5, y, z + 0.5),
                    zone
            );
            souls.add(soul);
        }
        return souls;
    }
}
