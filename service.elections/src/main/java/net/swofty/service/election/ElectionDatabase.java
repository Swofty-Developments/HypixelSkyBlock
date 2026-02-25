package net.swofty.service.election;

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

public record ElectionDatabase(String key) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> electionCollection;

    @Override
    public MongoDB connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        electionCollection = database.getCollection("elections");
        return this;
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public boolean exists() {
        Document query = new Document("_id", key);
        Document found = electionCollection.find(query).first();
        return found != null;
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = electionCollection.find(Filters.eq("_id", this.key)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", this.key);
            Document found = electionCollection.find(query).first();
            assert found != null;
            electionCollection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document newDoc = new Document("_id", this.key);
        newDoc.append(key, value);
        electionCollection.insertOne(newDoc);
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = electionCollection.find(query).first();
        if (found == null) {
            return false;
        }
        electionCollection.deleteOne(query);
        return true;
    }

    public static String loadElectionData() {
        Document doc = electionCollection.find(Filters.eq("_id", "election_data")).first();
        if (doc == null) return null;
        return doc.getString("data");
    }

    public static void saveElectionData(String serializedData) {
        Document query = new Document("_id", "election_data");
        Document existing = electionCollection.find(query).first();
        if (existing != null) {
            electionCollection.updateOne(query, Updates.set("data", serializedData));
        } else {
            Document newDoc = new Document("_id", "election_data");
            newDoc.append("data", serializedData);
            electionCollection.insertOne(newDoc);
        }
    }
}
