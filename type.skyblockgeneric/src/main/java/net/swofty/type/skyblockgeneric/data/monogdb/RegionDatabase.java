package net.swofty.type.skyblockgeneric.data.monogdb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.minestom.server.coordinate.Pos;
import net.swofty.commons.ServerType;
import net.swofty.type.generic.data.mongodb.MongoDB;
import net.swofty.type.skyblockgeneric.region.RegionType;
import net.swofty.type.skyblockgeneric.region.SkyBlockRegion;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public record RegionDatabase(String id) implements MongoDB {
	public static MongoDatabase database;
	public static MongoCollection<Document> collection;

	public static void connect(MongoClient client) {
		database = client.getDatabase("Minestom");
		collection = database.getCollection("regions");
	}

	public static List<SkyBlockRegion> getAllRegions() {
		List<SkyBlockRegion> regions = new ArrayList<>();
		for (Document doc : collection.find()) {
			String name = doc.getString("_id");
			RegionType type;
			try {
				type = RegionType.valueOf(doc.getString("type"));
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException("Region " + name + " has an invalid type: " + doc.getString("type"));
			}
			int x1 = doc.getInteger("x1");
			int y1 = doc.getInteger("y1");
			int z1 = doc.getInteger("z1");
			int x2 = doc.getInteger("x2");
			int y2 = doc.getInteger("y2");
			int z2 = doc.getInteger("z2");
			ServerType serverType = ServerType.getSkyblockServer(doc.getOrDefault("serverType", ServerType.SKYBLOCK_HUB.name()).toString());
			SkyBlockRegion region = new SkyBlockRegion(
					name,
					new Pos(x1, y1, z1),
					new Pos(x2, y2, z2),
					type,
					serverType);
			regions.add(region);
		}
		return regions;
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
}
