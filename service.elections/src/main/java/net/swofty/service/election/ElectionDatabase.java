package net.swofty.service.election;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import net.swofty.service.generic.MongoDB;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ElectionDatabase(String key) implements MongoDB {
    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> electionCollection;
    public static MongoCollection<Document> votesCollection;
    public static MongoCollection<Document> talliesCollection;

    @Override
    public MongoDB connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        electionCollection = database.getCollection("elections");
        votesCollection = database.getCollection("election-votes");
        talliesCollection = database.getCollection("election-tallies");
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
        if (doc == null) return def;
        return doc.get(key);
    }

    @Override
    public void insertOrUpdate(String key, Object value) {
        Document doc = new Document("_id", this.key).append(key, value);
        electionCollection.replaceOne(
                Filters.eq("_id", this.key),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = electionCollection.find(query).first();
        if (found == null) return false;
        electionCollection.deleteOne(query);
        return true;
    }

    public static String loadElectionData() {
        Document doc = electionCollection.find(Filters.eq("_id", "election_data")).first();
        if (doc == null) return null;
        return doc.getString("data");
    }

    public static void saveElectionData(String serializedData) {
        Document doc = new Document("_id", "election_data").append("data", serializedData);
        electionCollection.replaceOne(
                Filters.eq("_id", "election_data"),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    public static void castVote(String playerId, String candidateName, int electionYear) {
        String talliesDocId = "tallies_" + electionYear;

        try (ClientSession session = client.startSession()) {
            session.withTransaction(() -> {
                Document existingVote = votesCollection.find(session,
                        Filters.and(Filters.eq("_id", playerId), Filters.eq("electionYear", electionYear))
                ).first();

                if (existingVote != null) {
                    String oldCandidate = existingVote.getString("candidate");
                    if (oldCandidate != null && !oldCandidate.equals(candidateName)) {
                        talliesCollection.updateOne(session,
                                Filters.eq("_id", talliesDocId),
                                Updates.inc(oldCandidate, -1L)
                        );
                    }
                }

                Document voteDoc = new Document("_id", playerId)
                        .append("candidate", candidateName)
                        .append("electionYear", electionYear);
                votesCollection.replaceOne(session,
                        Filters.and(Filters.eq("_id", playerId), Filters.eq("electionYear", electionYear)),
                        voteDoc,
                        new ReplaceOptions().upsert(true)
                );

                if (existingVote == null || !candidateName.equals(existingVote.getString("candidate"))) {
                    talliesCollection.updateOne(session,
                            Filters.eq("_id", talliesDocId),
                            Updates.inc(candidateName, 1L),
                            new UpdateOptions().upsert(true)
                    );
                }
                return null;
            });
        }
    }

    public static String getPlayerVote(String playerId, int electionYear) {
        Document doc = votesCollection.find(
                Filters.and(Filters.eq("_id", playerId), Filters.eq("electionYear", electionYear))
        ).first();
        if (doc == null) return null;
        return doc.getString("candidate");
    }

    public static Map<String, Long> getTallies(int electionYear) {
        String talliesDocId = "tallies_" + electionYear;
        Document doc = talliesCollection.find(Filters.eq("_id", talliesDocId)).first();
        if (doc == null) return new HashMap<>();

        Map<String, Long> tallies = new HashMap<>();
        for (String key : doc.keySet()) {
            if (key.equals("_id")) continue;
            Object val = doc.get(key);
            if (val instanceof Number num) {
                tallies.put(key, num.longValue());
            }
        }
        return tallies;
    }

    public static void initTallies(int electionYear, List<String> candidateNames) {
        String talliesDocId = "tallies_" + electionYear;
        Document doc = new Document("_id", talliesDocId);
        for (String name : candidateNames) {
            doc.append(name, 0L);
        }
        talliesCollection.replaceOne(
                Filters.eq("_id", talliesDocId),
                doc,
                new ReplaceOptions().upsert(true)
        );
    }

    public static void clearVotesForYear(int electionYear) {
        votesCollection.deleteMany(Filters.eq("electionYear", electionYear));
        talliesCollection.deleteMany(Filters.eq("_id", "tallies_" + electionYear));
    }
}
