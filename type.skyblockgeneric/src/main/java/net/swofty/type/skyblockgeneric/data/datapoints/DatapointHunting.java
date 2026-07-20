package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.hunting.AttributeDefinition;
import net.swofty.type.skyblockgeneric.hunting.AttributeId;
import net.swofty.type.skyblockgeneric.hunting.AttributeRegistry;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class DatapointHunting extends SkyBlockDatapoint<DatapointHunting.HuntingData> {
    private static final Serializer<HuntingData> SERIALIZER = new Serializer<>() {
        @Override
        public String serialize(HuntingData value) {
            JSONObject json = new JSONObject();
            json.put("shards", new JSONObject(value.shards));
            json.put("syphoned", new JSONObject(value.syphoned));
            json.put("disabled", new JSONArray(value.disabled));
            json.put("discoveredAt", new JSONObject(value.discoveredAt));
            JSONArray traps = new JSONArray();
            value.traps.forEach(trap -> traps.put(trap.toJson()));
            json.put("traps", traps);
            return json.toString();
        }

        @Override
        public HuntingData deserialize(String json) {
            if (json == null || json.isBlank()) return new HuntingData();
            JSONObject object = new JSONObject(json);
            HuntingData data = new HuntingData();
            readIntMap(object.optJSONObject("shards"), data.shards);
            readIntMap(object.optJSONObject("syphoned"), data.syphoned);
            readLongMap(object.optJSONObject("discoveredAt"), data.discoveredAt);
            JSONArray disabled = object.optJSONArray("disabled");
            if (disabled != null) for (int i = 0; i < disabled.length(); i++) data.disabled.add(disabled.getString(i));
            JSONArray traps = object.optJSONArray("traps");
            if (traps != null)
                for (int i = 0; i < traps.length(); i++) data.traps.add(PlacedHuntrap.fromJson(traps.getJSONObject(i)));
            return data;
        }

        @Override
        public HuntingData clone(HuntingData value) {
            return new HuntingData(new HashMap<>(value.shards), new HashMap<>(value.syphoned),
                    new HashSet<>(value.disabled), new HashMap<>(value.discoveredAt), new ArrayList<>(value.traps));
        }

        private void readIntMap(JSONObject json, Map<String, Integer> destination) {
            if (json != null) json.keySet().forEach(key -> destination.put(key, json.optInt(key)));
        }

        private void readLongMap(JSONObject json, Map<String, Long> destination) {
            if (json != null) json.keySet().forEach(key -> destination.put(key, json.optLong(key)));
        }
    };

    public DatapointHunting(String key, HuntingData value) {
        super(key, value, SERIALIZER);
    }

    public DatapointHunting(String key) {
        this(key, new HuntingData());
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HuntingData {
        private Map<String, Integer> shards = new HashMap<>();
        private Map<String, Integer> syphoned = new HashMap<>();
        private Set<String> disabled = new HashSet<>();
        private Map<String, Long> discoveredAt = new HashMap<>();
        private List<PlacedHuntrap> traps = new ArrayList<>();

        public int shardCount(String id) {
            return shards.getOrDefault(id, 0);
        }

        public int shardCount(AttributeId id) {
            return shardCount(id.toString());
        }

        public void addShards(String id, int amount) {
            if (AttributeRegistry.get(id) == null || amount <= 0) return;
            shards.merge(id, amount, Integer::sum);
            discoveredAt.putIfAbsent(id, System.currentTimeMillis());
        }

        public void addShards(AttributeId id, int amount) {
            addShards(id.toString(), amount);
        }

        public boolean removeShards(String id, int amount) {
            if (amount <= 0 || shardCount(id) < amount) return false;
            shards.compute(id, (ignored, count) -> count == amount ? null : count - amount);
            return true;
        }

        public boolean removeShards(AttributeId id, int amount) {
            return removeShards(id.toString(), amount);
        }

        public int syphoned(String id) {
            return syphoned.getOrDefault(id, 0);
        }

        public int syphoned(AttributeId id) {
            return syphoned(id.toString());
        }

        public int level(String id) {
            AttributeDefinition definition = AttributeRegistry.get(id);
            return definition == null ? 0 : definition.rarity().levelFor(syphoned(id));
        }

        public int level(AttributeId id) {
            return level(id.toString());
        }

        public int syphon(String id, int amount) {
            AttributeDefinition definition = AttributeRegistry.get(id);
            if (definition == null || amount <= 0) return 0;
            int maximum = definition.rarity().cumulativeForLevel(10);
            int usable = Math.min(Math.min(amount, shardCount(id)), maximum - syphoned(id));
            if (usable <= 0 || !removeShards(id, usable)) return 0;
            syphoned.merge(id, usable, Integer::sum);
            disabled.remove(id);
            return usable;
        }

        public int syphon(AttributeId id, int amount) {
            return syphon(id.toString(), amount);
        }

        public boolean enabled(String id) {
            return level(id) > 0 && !disabled.contains(id);
        }

        public boolean enabled(AttributeId id) {
            return enabled(id.toString());
        }

        public void toggle(String id) {
            if (level(id) == 0) return;
            if (!disabled.add(id)) disabled.remove(id);
        }

        public void toggle(AttributeId id) {
            toggle(id.toString());
        }

        public int uniqueAttributes() {
            return (int) syphoned.keySet().stream().filter(id -> level(id) > 0).count();
        }
    }

    public record PlacedHuntrap(String world, double x, double y, double z, String tier,
                                long placedAt, long catchAt, String caughtShard) {
        JSONObject toJson() {
            return new JSONObject().put("world", world).put("x", x).put("y", y).put("z", z)
                    .put("tier", tier).put("placedAt", placedAt).put("catchAt", catchAt)
                    .put("caughtShard", caughtShard == null ? JSONObject.NULL : caughtShard);
        }

        static PlacedHuntrap fromJson(JSONObject json) {
            return new PlacedHuntrap(json.optString("world"), json.optDouble("x"), json.optDouble("y"),
                    json.optDouble("z"), json.optString("tier"), json.optLong("placedAt"),
                    json.optLong("catchAt"), json.isNull("caughtShard") ? null : json.optString("caughtShard"));
        }
    }
}
