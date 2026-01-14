package net.swofty.type.generic.data.handlers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public final class PunishmentsDatabase {

    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    private PunishmentsDatabase() {}

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("punishments");
    }
}
