package net.swofty.service.api;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.swofty.commons.protocol.objects.api.APIAuthenticateCodeProtocolObject;
import net.swofty.service.generic.MongoDB;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record APIAdminDatabase() {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public void connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("api-admin");
    }

    public static void replaceOrInsert(APIAdminDatabaseObject object) {
        Document document = object.toDocument();
        if (collection.find(new Document("_id", object.sessionId)).first() != null) {
            collection.replaceOne(new Document("_id", object.sessionId), document);
        } else {
            collection.insertOne(document);
        }
    }

    public static @Nullable APIAdminDatabaseObject getFromCode(String authCode) {
        Document document = collection.find(new Document("authCode", authCode)).first();
        if (document == null) {
            return null;
        }
        return APIAdminDatabaseObject.fromDocument(document);
    }

    public @Nullable APIAdminDatabaseObject getFromSessionId(String sessionId) {
        Document document = collection.find(new Document("_id", sessionId)).first();
        if (document == null) {
            return null;
        }
        return APIAdminDatabaseObject.fromDocument(document);
    }
}
