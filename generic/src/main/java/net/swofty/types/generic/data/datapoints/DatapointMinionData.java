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
                sb.append("{");
                for (String minion : value.craftedMinions()) {
                    sb.append("\"").append(minion).append("\":").append("true").append(",");
                }
                if (sb.charAt(sb.length() - 1) == ',') {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("}");
                return sb.toString();
            }

            @Override
            public DatapointMinionData.ProfileMinionData deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                List<String> craftedMinions = List.of(jsonObject.keySet().toArray(new String[0]));
                return new ProfileMinionData(craftedMinions);
            }
        });
    }

    public DatapointMinionData(String key) {
        this(key, new ProfileMinionData(new ArrayList<>()));
    }

    public record ProfileMinionData(List<String> craftedMinions) {
        private static final Map<Integer, Integer> SLOTS_FOR_CRAFTED = new HashMap<>(Map.of(
                0, 5,
                5, 6,
                11, 7
        ));

        public int getSlots() {
            for (Map.Entry<Integer, Integer> entry : SLOTS_FOR_CRAFTED.entrySet()) {
                if (craftedMinions.size() <= entry.getKey()) {
                    return entry.getValue();
                }
            }
            return 0;
        }
    }
}
