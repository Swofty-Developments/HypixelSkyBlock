package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointTrophyFish extends SkyBlockDatapoint<DatapointTrophyFish.TrophyFishData> {
    private static final Serializer<TrophyFishData> serializer = new Serializer<>() {
        @Override
        public String serialize(TrophyFishData value) {
            JSONObject object = new JSONObject();
            JSONObject fishObject = new JSONObject();
            value.getFish().forEach((fishId, progress) -> {
                JSONObject progressObject = new JSONObject();
                progressObject.put("bronze", progress.getBronze());
                progressObject.put("silver", progress.getSilver());
                progressObject.put("gold", progress.getGold());
                progressObject.put("diamond", progress.getDiamond());
                progressObject.put("totalCatches", progress.getTotalCatches());
                fishObject.put(fishId, progressObject);
            });
            object.put("fish", fishObject);
            return object.toString();
        }

        @Override
        public TrophyFishData deserialize(String json) {
            TrophyFishData data = new TrophyFishData();
            if (json == null || json.isEmpty()) {
                return data;
            }

            JSONObject object = new JSONObject(json);
            JSONObject fishObject = object.optJSONObject("fish");
            if (fishObject == null) {
                return data;
            }

            for (String fishId : fishObject.keySet()) {
                JSONObject progressObject = fishObject.getJSONObject(fishId);
                data.getFish().put(fishId, new FishProgress(
                    progressObject.optInt("bronze", 0),
                    progressObject.optInt("silver", 0),
                    progressObject.optInt("gold", 0),
                    progressObject.optInt("diamond", 0),
                    progressObject.optInt("totalCatches", 0)
                ));
            }
            return data;
        }

        @Override
        public TrophyFishData clone(TrophyFishData value) {
            Map<String, FishProgress> fish = new HashMap<>();
            value.getFish().forEach((fishId, progress) ->
                fish.put(fishId, new FishProgress(
                    progress.getBronze(),
                    progress.getSilver(),
                    progress.getGold(),
                    progress.getDiamond(),
                    progress.getTotalCatches()
                )));
            return new TrophyFishData(fish);
        }
    };

    public DatapointTrophyFish(String key, TrophyFishData value) {
        super(key, value, serializer);
    }

    public DatapointTrophyFish(String key) {
        this(key, new TrophyFishData());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrophyFishData {
        private Map<String, FishProgress> fish = new HashMap<>();

        public FishProgress getProgress(String fishId) {
            return fish.computeIfAbsent(fishId, ignored -> new FishProgress());
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FishProgress {
        private int bronze;
        private int silver;
        private int gold;
        private int diamond;
        private int totalCatches;

        public void increment(String tier) {
            switch (tier.toUpperCase()) {
                case "DIAMOND" -> diamond++;
                case "GOLD" -> gold++;
                case "SILVER" -> silver++;
                default -> bronze++;
            }
            totalCatches++;
        }

        public boolean hasTier(String tier) {
            return switch (tier.toUpperCase()) {
                case "DIAMOND" -> diamond > 0;
                case "GOLD" -> gold > 0;
                case "SILVER" -> silver > 0;
                default -> bronze > 0;
            };
        }
    }
}
