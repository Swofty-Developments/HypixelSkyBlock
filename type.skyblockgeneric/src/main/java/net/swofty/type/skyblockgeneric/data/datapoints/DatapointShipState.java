package net.swofty.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.swofty.commons.protocol.Serializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatapointShipState extends SkyBlockDatapoint<DatapointShipState.ShipState> {
    private static final Serializer<ShipState> serializer = new Serializer<>() {
        @Override
        public String serialize(ShipState value) {
            JSONObject object = new JSONObject();
            object.put("shipName", value.getShipName());
            object.put("helm", value.getHelm());
            object.put("engine", value.getEngine());
            object.put("hull", value.getHull());
            object.put("destinations", value.getUnlockedDestinations());
            return object.toString();
        }

        @Override
        public ShipState deserialize(String json) {
            if (json == null || json.isEmpty()) {
                return new ShipState();
            }

            JSONObject object = new JSONObject(json);
            List<String> destinations = new ArrayList<>();
            JSONArray array = object.optJSONArray("destinations");
            if (array != null) {
                for (Object entry : array) {
                    destinations.add(String.valueOf(entry));
                }
            }

            return new ShipState(
                object.optString("shipName", "Zephyr"),
                object.optString("helm", "CRACKED_SHIP_HELM"),
                emptyToNull(object.optString("engine", "")),
                object.optString("hull", "RUSTY_SHIP_HULL"),
                destinations
            );
        }

        @Override
        public ShipState clone(ShipState value) {
            return new ShipState(
                value.getShipName(),
                value.getHelm(),
                value.getEngine(),
                value.getHull(),
                new ArrayList<>(value.getUnlockedDestinations())
            );
        }
    };

    public DatapointShipState(String key, ShipState value) {
        super(key, value, serializer);
    }

    public DatapointShipState(String key) {
        this(key, new ShipState());
    }

    private static String emptyToNull(String value) {
        return value == null || value.isEmpty() ? null : value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipState {
        private String shipName = "Zephyr";
        private String helm = "CRACKED_SHIP_HELM";
        private String engine = null;
        private String hull = "RUSTY_SHIP_HULL";
        private List<String> unlockedDestinations = new ArrayList<>();

        public boolean hasDestination(String destinationId) {
            return unlockedDestinations.contains(destinationId);
        }

        public void unlockDestination(String destinationId) {
            if (!unlockedDestinations.contains(destinationId)) {
                unlockedDestinations.add(destinationId);
            }
        }
    }
}
