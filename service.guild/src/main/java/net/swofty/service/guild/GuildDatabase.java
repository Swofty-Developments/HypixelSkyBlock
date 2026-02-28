package net.swofty.service.guild;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import net.swofty.commons.guild.GuildData;
import net.swofty.service.generic.MongoDB;
import org.bson.Document;

import java.util.UUID;

public record GuildDatabase(String id) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> guildCollection;
    public static MongoCollection<Document> playerGuildCollection;

    @Override
    public MongoDB connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        guildCollection = database.getCollection("guilds");
        playerGuildCollection = database.getCollection("player-guilds");

        guildCollection.createIndex(Indexes.ascending("name"), new IndexOptions().unique(true));
        playerGuildCollection.createIndex(Indexes.ascending("guildId"));
        return this;
    }

    public static GuildData getGuild(UUID guildId) {
        Document doc = guildCollection.find(Filters.eq("_id", guildId.toString())).first();
        if (doc == null) return null;
        return GuildData.getStaticSerializer().deserialize(doc.getString("data"));
    }

    public static void saveGuild(GuildData guild) {
        String serialized = guild.getSerializer().serialize(guild);
        String guildId = guild.getGuildId().toString();

        Document query = new Document("_id", guildId);
        Document existing = guildCollection.find(query).first();

        if (existing != null) {
            guildCollection.updateOne(query,
                Updates.combine(
                    Updates.set("data", serialized),
                    Updates.set("name", guild.getName().toLowerCase())
                ));
        } else {
            Document doc = new Document("_id", guildId);
            doc.append("data", serialized);
            doc.append("name", guild.getName().toLowerCase());
            guildCollection.insertOne(doc);
        }
    }

    public static void deleteGuild(UUID guildId) {
        guildCollection.deleteOne(Filters.eq("_id", guildId.toString()));
        playerGuildCollection.deleteMany(Filters.eq("guildId", guildId.toString()));
    }

    public static void mapPlayerToGuild(UUID playerUuid, UUID guildId) {
        String playerId = playerUuid.toString();
        Document query = new Document("_id", playerId);
        Document existing = playerGuildCollection.find(query).first();

        if (existing != null) {
            playerGuildCollection.updateOne(query, Updates.set("guildId", guildId.toString()));
        } else {
            Document doc = new Document("_id", playerId);
            doc.append("guildId", guildId.toString());
            playerGuildCollection.insertOne(doc);
        }
    }

    public static void removePlayerMapping(UUID playerUuid) {
        playerGuildCollection.deleteOne(Filters.eq("_id", playerUuid.toString()));
    }

    public static UUID getPlayerGuildId(UUID playerUuid) {
        Document doc = playerGuildCollection.find(Filters.eq("_id", playerUuid.toString())).first();
        if (doc == null) return null;
        String guildId = doc.getString("guildId");
        return guildId != null ? UUID.fromString(guildId) : null;
    }

    public static boolean guildNameExists(String name) {
        return guildCollection.find(Filters.eq("name", name.toLowerCase())).first() != null;
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = guildCollection.find(Filters.eq("_id", id)).first();
        if (doc == null) return def;
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", id);
            Document found = guildCollection.find(query).first();
            assert found != null;
            guildCollection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document newDoc = new Document("_id", id);
        newDoc.append(key, value);
        guildCollection.insertOne(newDoc);
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = guildCollection.find(query).first();
        if (found == null) return false;
        guildCollection.deleteOne(query);
        return true;
    }

    @Override
    public boolean exists() {
        Document query = new Document("_id", id);
        return guildCollection.find(query).first() != null;
    }
}
