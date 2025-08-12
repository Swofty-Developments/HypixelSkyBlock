package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatapointMinionData extends SkyBlockDatapoint<DatapointMinionData.ProfileMinionData> {

    public DatapointMinionData(String key, DatapointMinionData.ProfileMinionData value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(DatapointMinionData.ProfileMinionData value) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");

                for (Map.Entry<String, List<Integer>> entry : value.craftedMinions) {
                    sb.append(entry.getKey()).append(":[");
                    entry.getValue().forEach(tier -> sb.append(tier).append(","));
                    if (sb.length() > 1) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append("],");
                }

                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("}");
                return sb.toString();
            }

            @Override
            public DatapointMinionData.ProfileMinionData deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                List<Map.Entry<String, List<Integer>>> craftedMinions = new ArrayList<>();
                for (String key : jsonObject.keySet()) {
                    List<Integer> list = new ArrayList<>();
                    for (Object object : jsonObject.getJSONArray(key)) {
                        list.add((int) object);
                    }
                    craftedMinions.add(Map.entry(key, list));
                }
                return new ProfileMinionData(craftedMinions);
            }

            @Override
            public ProfileMinionData clone(ProfileMinionData value) {
                return new ProfileMinionData(new ArrayList<>(value.craftedMinions));
            }
        });
    }

    public DatapointMinionData(String key) {
        this(key, new ProfileMinionData(new ArrayList<>()));
    }

    public record ProfileMinionData(List<Map.Entry<String, List<Integer>>> craftedMinions) {
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
