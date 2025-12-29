package net.swofty.service.friend;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.swofty.commons.friend.FriendData;
import net.swofty.commons.friend.PendingFriendRequest;
import net.swofty.service.generic.MongoDB;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record FriendDatabase(String playerId) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> friendDataCollection;
    public static MongoCollection<Document> pendingRequestsCollection;

    @Override
    public MongoDB connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        friendDataCollection = database.getCollection("friend-data");
        pendingRequestsCollection = database.getCollection("pending-friend-requests");
        return this;
    }

    public FriendData getFriendData(UUID playerUuid) {
        Document doc = friendDataCollection.find(Filters.eq("_id", playerUuid.toString())).first();
        if (doc == null) {
            return null;
        }
        String data = doc.getString("data");
        return FriendData.getStaticSerializer().deserialize(data);
    }

    public void saveFriendData(FriendData data) {
        String serialized = data.getSerializer().serialize(data);
        String id = data.getPlayerUuid().toString();

        Document query = new Document("_id", id);
        Document existing = friendDataCollection.find(query).first();

        if (existing != null) {
            friendDataCollection.updateOne(query, Updates.set("data", serialized));
        } else {
            Document newDoc = new Document("_id", id);
            newDoc.append("data", serialized);
            friendDataCollection.insertOne(newDoc);
        }
    }

    public List<PendingFriendRequest> getPendingRequestsFor(UUID playerUuid) {
        List<PendingFriendRequest> requests = new ArrayList<>();
        FindIterable<Document> results = pendingRequestsCollection.find(Filters.eq("to", playerUuid.toString()));
        for (Document doc : results) {
            String data = doc.getString("data");
            requests.add(PendingFriendRequest.getStaticSerializer().deserialize(data));
        }
        return requests;
    }

    public List<PendingFriendRequest> getPendingRequestsFrom(UUID playerUuid) {
        List<PendingFriendRequest> requests = new ArrayList<>();
        FindIterable<Document> results = pendingRequestsCollection.find(Filters.eq("from", playerUuid.toString()));
        for (Document doc : results) {
            String data = doc.getString("data");
            requests.add(PendingFriendRequest.getStaticSerializer().deserialize(data));
        }
        return requests;
    }

    public List<PendingFriendRequest> getAllPendingRequests() {
        List<PendingFriendRequest> requests = new ArrayList<>();
        FindIterable<Document> results = pendingRequestsCollection.find();
        for (Document doc : results) {
            String data = doc.getString("data");
            requests.add(PendingFriendRequest.getStaticSerializer().deserialize(data));
        }
        return requests;
    }

    public void addPendingRequest(PendingFriendRequest request) {
        String id = request.getFrom().toString() + "_" + request.getTo().toString();
        String serialized = request.getSerializer().serialize(request);

        Document doc = new Document("_id", id);
        doc.append("from", request.getFrom().toString());
        doc.append("to", request.getTo().toString());
        doc.append("data", serialized);
        pendingRequestsCollection.insertOne(doc);
    }

    public void removePendingRequest(UUID from, UUID to) {
        String id = from.toString() + "_" + to.toString();
        pendingRequestsCollection.deleteOne(Filters.eq("_id", id));
    }

    public boolean hasPendingRequest(UUID from, UUID to) {
        String id = from.toString() + "_" + to.toString();
        Document doc = pendingRequestsCollection.find(Filters.eq("_id", id)).first();
        return doc != null;
    }

    public PendingFriendRequest getPendingRequest(UUID from, UUID to) {
        String id = from.toString() + "_" + to.toString();
        Document doc = pendingRequestsCollection.find(Filters.eq("_id", id)).first();
        if (doc == null) {
            return null;
        }
        return PendingFriendRequest.getStaticSerializer().deserialize(doc.getString("data"));
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = friendDataCollection.find(Filters.eq("_id", playerId)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", playerId);
            Document found = friendDataCollection.find(query).first();
            assert found != null;
            friendDataCollection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document newDoc = new Document("_id", playerId);
        newDoc.append(key, value);
        friendDataCollection.insertOne(newDoc);
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = friendDataCollection.find(query).first();
        if (found == null) {
            return false;
        }
        friendDataCollection.deleteOne(query);
        return true;
    }

    public boolean exists() {
        Document query = new Document("_id", playerId);
        Document found = friendDataCollection.find(query).first();
        return found != null;
    }
}
