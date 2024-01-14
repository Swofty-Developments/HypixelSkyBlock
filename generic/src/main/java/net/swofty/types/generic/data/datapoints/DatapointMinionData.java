package net.swofty.types.generic.data.datapoints;

import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.ItemType;
import net.swofty.types.generic.serializer.Serializer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointMinionData extends Datapoint<DatapointMinionData.ProfileMinionData> {

    public DatapointMinionData(String key, DatapointMinionData.ProfileMinionData value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(DatapointMinionData.ProfileMinionData value) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Integer> entry : value.craftedMinions) {
                    sb.append(entry.getKey()).append(":").append(entry.getValue()).append(",");
                }
                return sb.toString();
            }

            @Override
            public DatapointMinionData.ProfileMinionData deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                List<Map.Entry<String, Integer>> craftedMinions = new ArrayList<>();
                for (String key : jsonObject.keySet()) {
                    craftedMinions.add(Map.entry(key, jsonObject.getInt(key)));
                }
                return new ProfileMinionData(craftedMinions);
            }
        });
    }

    public DatapointMinionData(String key) {
        this(key, new ProfileMinionData(new ArrayList<>()));
    }

    public record ProfileMinionData(List<Map.Entry<String, Integer>> craftedMinions) {
        private static final Map<Integer, Integer> SLOTS_FOR_CRAFTED = new HashMap<>(Map.of(
                0, 5,
                5, 6,
                11, 7
        ));

        public int getUniqueMinions() {
            // Check for every unique key in the map
            return (int) craftedMinions.stream().map(Map.Entry::getKey).distinct().count();
        }

        public int getSlots() {
            for (Map.Entry<Integer, Integer> entry : SLOTS_FOR_CRAFTED.entrySet()) {
                if (getUniqueMinions() <= entry.getKey()) {
                    return entry.getValue();
                }
            }
            return 0;
        }
    }
}
