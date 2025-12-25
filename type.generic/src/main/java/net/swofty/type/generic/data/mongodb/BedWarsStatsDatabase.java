package net.swofty.type.generic.data.mongodb;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Accumulators;
import net.swofty.commons.bedwars.BedwarsGameType;
import net.swofty.commons.bedwars.BedwarsLeaderboardMode;
import net.swofty.commons.bedwars.BedwarsStatType;
import org.bson.Document;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BedWarsStatsDatabase {
	public static MongoDatabase database;
	public static MongoCollection<Document> collection;

	public static void connect(MongoClient client) {
		database = client.getDatabase("Minestom");
		collection = database.getCollection("bedwars_stat_events");

		// Create indexes for efficient queries
		collection.createIndex(Indexes.compoundIndex(
				Indexes.ascending("statType"),
				Indexes.ascending("gameMode"),
				Indexes.descending("timestamp")
		));
		collection.createIndex(Indexes.compoundIndex(
				Indexes.ascending("playerUuid"),
				Indexes.ascending("statType"),
				Indexes.descending("timestamp")
		));
	}

	public static void recordStatEvent(UUID playerUuid, BedwarsStatType statType, BedwarsGameType gameMode, int amount) {
		Document event = new Document()
				.append("playerUuid", playerUuid.toString())
				.append("statType", statType.name())
				.append("gameMode", gameMode.name())
				.append("amount", amount)
				.append("timestamp", new Date());

		collection.insertOne(event);
	}

	public static void recordStatEventAsync(UUID playerUuid, BedwarsStatType statType, BedwarsGameType gameMode, int amount) {
		Thread.startVirtualThread(() -> recordStatEvent(playerUuid, statType, gameMode, amount));
	}

	public static Map<UUID, Long> aggregateStats(BedwarsStatType statType, BedwarsLeaderboardMode mode, Date since) {
		List<Document> pipeline = new ArrayList<>();

		// Match by stat type and time
		Document matchFilter = new Document("statType", statType.name());
		if (since != null) {
			matchFilter.append("timestamp", new Document("$gte", since));
		}

		// Add mode filter if not ALL
		if (mode != BedwarsLeaderboardMode.ALL) {
			List<String> includedModes = new ArrayList<>();
			for (BedwarsGameType gameType : BedwarsGameType.values()) {
				if (mode.includes(gameType)) {
					includedModes.add(gameType.name());
				}
			}
			matchFilter.append("gameMode", new Document("$in", includedModes));
		}

		pipeline.add(new Document("$match", matchFilter));

		// Group by player and sum amounts
		pipeline.add(new Document("$group", new Document()
				.append("_id", "$playerUuid")
				.append("total", new Document("$sum", "$amount"))
		));

		Map<UUID, Long> results = new HashMap<>();
		AggregateIterable<Document> aggregateResult = collection.aggregate(pipeline);

		for (Document doc : aggregateResult) {
			String uuidStr = doc.getString("_id");
			long total = doc.get("total", Number.class).longValue();
			results.put(UUID.fromString(uuidStr), total);
		}

		return results;
	}

	public static Date getStartOfDay() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		return Date.from(now.truncatedTo(ChronoUnit.DAYS).toInstant());
	}

	public static Date getStartOfWeek() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime startOfWeek = now.truncatedTo(ChronoUnit.DAYS)
				.minusDays(now.getDayOfWeek().getValue() - 1);
		return Date.from(startOfWeek.toInstant());
	}

	public static Date getStartOfMonth() {
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime startOfMonth = now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
		return Date.from(startOfMonth.toInstant());
	}

	public static long getPlayerTotalStat(UUID playerUuid, BedwarsStatType statType, BedwarsLeaderboardMode mode, Date since) {
		List<Document> pipeline = new ArrayList<>();

		Document matchFilter = new Document()
				.append("playerUuid", playerUuid.toString())
				.append("statType", statType.name());

		if (since != null) {
			matchFilter.append("timestamp", new Document("$gte", since));
		}

		if (mode != BedwarsLeaderboardMode.ALL) {
			List<String> includedModes = new ArrayList<>();
			for (BedwarsGameType gameType : BedwarsGameType.values()) {
				if (mode.includes(gameType)) {
					includedModes.add(gameType.name());
				}
			}
			matchFilter.append("gameMode", new Document("$in", includedModes));
		}

		pipeline.add(new Document("$match", matchFilter));
		pipeline.add(new Document("$group", new Document()
				.append("_id", null)
				.append("total", new Document("$sum", "$amount"))
		));

		Document result = collection.aggregate(pipeline).first();
		if (result == null) {
			return 0;
		}
		return result.get("total", Number.class).longValue();
	}

	public static void cleanupOldEvents(int daysToKeep) {
		Date cutoff = Date.from(Instant.now().minus(daysToKeep, ChronoUnit.DAYS));
		collection.deleteMany(Filters.lt("timestamp", cutoff));
	}
}
