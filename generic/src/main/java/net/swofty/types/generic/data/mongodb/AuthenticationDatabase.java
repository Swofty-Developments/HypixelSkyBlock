package net.swofty.types.generic.data.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public record AuthenticationDatabase(UUID id) implements MongoDB {
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public static void connect(MongoClient client) {
        database = client.getDatabase("Minestom");
        collection = database.getCollection("authentication");
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
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
        Document query = new Document("_id", id);
        return collection.find(query).first();
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = collection.find(query).first();

        if (found == null) {
            return false;
        }

        collection.deleteOne(query);
        return true;
    }

    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", id);
            Document found = collection.find(query).first();

            assert found != null;
            collection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document New = new Document("_id", id);
        New.append(key, value);
        collection.insertOne(New);
    }

    public boolean exists() {
        Document query = new Document("_id", id);
        Document found = collection.find(query).first();
        return found != null;
    }

    public @Nullable AuthenticationData getAuthenticationData() {
        Document document = collection.find(new Document("_id", id.toString())).first();
        if (document == null) {
            return null;
        }
        return AuthenticationData.deserialize(document);
    }

    public void setAuthenticationData(AuthenticationData data) {
        Document document = data.serialize();
        document.put("_id", id.toString());
        if (collection.find(Filters.eq("_id", id.toString())).first() == null) {
            collection.insertOne(document);
            return;
        }
        collection.replaceOne(Filters.eq("_id", id.toString()), document);
    }

    public static AuthenticationData makeFromPassword(String password) {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltString = Base64.getEncoder().encodeToString(salt);

        // Hash the password with the salt
        String hash = hashPassword(password, saltString);

        return new AuthenticationData(hash, saltString);
    }

    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public record AuthenticationData(String hash, String salt) {
        public static AuthenticationData deserialize(Document document) {
            return new AuthenticationData(document.getString("hash"), document.getString("salt"));
        }

        public Document serialize() {
            return new Document("hash", hash).append("salt", salt);
        }

        public boolean matches(String rawInput) {
            String hashedInput = hashPassword(rawInput, salt);
            return hash.equals(hashedInput);
        }
    }
}