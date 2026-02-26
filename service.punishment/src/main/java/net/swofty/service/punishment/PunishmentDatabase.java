package net.swofty.service.punishment;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.swofty.service.generic.MongoDB;
import org.bson.Document;

public record PunishmentDatabase(String playerId) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> punishmentCollection;

    @Override
    public MongoDB connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        punishmentCollection = database.getCollection("punishments");
        return this;
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public boolean exists() {
        Document query = new Document("_id", playerId);
        Document found = punishmentCollection.find(query).first();
        return found != null;
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = punishmentCollection.find(Filters.eq("_id", playerId)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", playerId);
            Document found = punishmentCollection.find(query).first();
            assert found != null;
            punishmentCollection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document newDoc = new Document("_id", playerId);
        newDoc.append(key, value);
        punishmentCollection.insertOne(newDoc);
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = punishmentCollection.find(query).first();
        if (found == null) {
            return false;
        }
        punishmentCollection.deleteOne(query);
        return true;
    }
}
