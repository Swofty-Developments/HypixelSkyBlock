package net.swofty.velocity.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public record CoopDatabase(UUID id) {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("coop");
    }

    public Document getDocument() {
        Document query = new Document("_id", id.toString());
        return collection.find(query).first();
    }

    public static Document getFromMemberProfile(UUID memberProfile) {
        // Search through all coop documents and find the one that contains the UUID in the memberProfiles list
        for (Document document : collection.find()) {
            List<String> memberProfiles = (List<String>) document.get("memberProfiles");

            if (memberProfiles.contains(memberProfile.toString())) {
                return document;
            }
        }

        return null;
    }
}
