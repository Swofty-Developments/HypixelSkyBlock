package net.swofty.type.skyblockgeneric.data.datapoints;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import net.swofty.type.skyblockgeneric.slayer.SlayerQuest;
import net.swofty.type.skyblockgeneric.slayer.SlayerTier;
import net.swofty.type.skyblockgeneric.slayer.SlayerType;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public class DatapointSlayer extends SkyBlockDatapoint<DatapointSlayer.SlayerData> {
    private static final Serializer<SlayerData> serializer = new Serializer<>() {
        @Override
        public String serialize(SlayerData value) {
            JSONObject object = new JSONObject();
            JSONObject progressObject = new JSONObject();
            value.getProgress().forEach((type, progress) -> {
                JSONObject entry = new JSONObject();
                entry.put("xp", progress.getXp());
                JSONObject completions = new JSONObject();
                progress.getCompletions().forEach((tier, amount) -> completions.put(tier.name(), amount));
                entry.put("completions", completions);
                progressObject.put(type.name(), entry);
            });
            object.put("progress", progressObject);

            if (value.getActiveQuest() != null) {
                SlayerQuest quest = value.getActiveQuest();
                JSONObject questObject = new JSONObject();
                questObject.put("type", quest.type().name());
                questObject.put("tier", quest.tier().name());
                questObject.put("startedAt", quest.startedAt());
                questObject.put("combatXp", quest.combatXp());
                questObject.put("bossSpawned", quest.bossSpawned());
                if (quest.bossUuid() != null) {
                    questObject.put("bossUuid", quest.bossUuid().toString());
                }
                object.put("activeQuest", questObject);
            }

            return object.toString();
        }

        @Override
        public SlayerData deserialize(String json) {
            SlayerData data = new SlayerData();
            if (json == null || json.isEmpty()) {
                return data;
            }

            JSONObject object = new JSONObject(json);
            JSONObject progressObject = object.optJSONObject("progress");
            if (progressObject != null) {
                for (String typeId : progressObject.keySet()) {
                    SlayerType type = SlayerType.valueOf(typeId);
                    JSONObject entry = progressObject.getJSONObject(typeId);
                    SlayerProgress progress = new SlayerProgress();
                    progress.setXp(entry.optInt("xp", 0));
                    JSONObject completions = entry.optJSONObject("completions");
                    if (completions != null) {
                        for (String tierId : completions.keySet()) {
                            progress.getCompletions().put(SlayerTier.valueOf(tierId), completions.optInt(tierId, 0));
                        }
                    }
                    data.getProgress().put(type, progress);
                }
            }

            JSONObject questObject = object.optJSONObject("activeQuest");
            if (questObject != null) {
                data.setActiveQuest(new SlayerQuest(
                    SlayerType.valueOf(questObject.getString("type")),
                    SlayerTier.valueOf(questObject.getString("tier")),
                    questObject.optLong("startedAt", System.currentTimeMillis()),
                    questObject.optInt("combatXp", 0),
                    questObject.optBoolean("bossSpawned", false),
                    questObject.has("bossUuid") ? UUID.fromString(questObject.getString("bossUuid")) : null
                ));
            }
            return data;
        }

        @Override
        public SlayerData clone(SlayerData value) {
            SlayerData clone = new SlayerData();
            value.getProgress().forEach((type, progress) -> {
                SlayerProgress copied = new SlayerProgress(progress.getXp(), new EnumMap<>(progress.getCompletions()));
                clone.getProgress().put(type, copied);
            });
            clone.setActiveQuest(value.getActiveQuest());
            return clone;
        }
    };

    public DatapointSlayer(String key, SlayerData value) {
        super(key, value, serializer);
    }

    public DatapointSlayer(String key) {
        this(key, new SlayerData());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlayerData {
        private Map<SlayerType, SlayerProgress> progress = new EnumMap<>(SlayerType.class);
        private @Nullable SlayerQuest activeQuest;

        public SlayerProgress progress(SlayerType type) {
            return progress.computeIfAbsent(type, ignored -> new SlayerProgress());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlayerProgress {
        private int xp;
        private Map<SlayerTier, Integer> completions = new EnumMap<>(SlayerTier.class);

        public int completions(SlayerTier tier) {
            return completions.getOrDefault(tier, 0);
        }

        public void addCompletion(SlayerTier tier) {
            completions.put(tier, completions(tier) + 1);
        }
    }
}
