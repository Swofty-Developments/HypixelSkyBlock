package net.swofty.types.generic.data.datapoints;

import lombok.Getter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.user.fairysouls.FairySoulExchangeLevels;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatapointFairySouls extends Datapoint<DatapointFairySouls.PlayerFairySouls> {
    public static Serializer<PlayerFairySouls> serializer = new Serializer<PlayerFairySouls>() {

        @Override
        public String serialize(PlayerFairySouls value) {
            JSONObject json = new JSONObject();
            json.put("collectedFairySouls", value.collectedFairySouls);
            json.put("exchangedFairySouls", value.exchangedFairySouls);
            return json.toString();
        }

        @Override
        public PlayerFairySouls deserialize(String json) {
            JSONObject jsonObject = new JSONObject(json);
            PlayerFairySouls fairySouls = new PlayerFairySouls();
            jsonObject.getJSONArray("collectedFairySouls").forEach(soul -> fairySouls.collectedFairySouls.add((Integer) soul));
            jsonObject.getJSONArray("exchangedFairySouls").forEach(soul -> fairySouls.exchangedFairySouls.add((Integer) soul));
            return fairySouls;
        }

        @Override
        public PlayerFairySouls clone(PlayerFairySouls value) {
            PlayerFairySouls clone = new PlayerFairySouls();
            clone.collectedFairySouls.addAll(value.collectedFairySouls);
            clone.exchangedFairySouls.addAll(value.exchangedFairySouls);
            return clone;
        }
    };

    public DatapointFairySouls(String key, PlayerFairySouls value) {
        super(key, value, serializer);
    }

    public DatapointFairySouls(String key) {
        super(key, new PlayerFairySouls(), serializer);
    }

    @Getter
    public static class PlayerFairySouls {
        @Getter
        private final List<Integer> collectedFairySouls = new ArrayList<>();
        private final List<Integer> exchangedFairySouls = new ArrayList<>();

        public boolean hasCollectedFairySouls(int fairyKey) {
            return collectedFairySouls.contains(fairyKey);
        }

        public List<Integer> getAllFairySouls() {
            List<Integer> allFairySouls = new ArrayList<>();
            allFairySouls.addAll(collectedFairySouls);
            allFairySouls.addAll(exchangedFairySouls);
            return allFairySouls;
        }

        public void exchange() {
            // Swap 5 collected fairy souls into exchangedFairySouls
            for (int i = 0; i < 5; i++) {
                if (collectedFairySouls.isEmpty()) break;
                exchangedFairySouls.add(collectedFairySouls.removeFirst());
            }
        }

        public void addCollectedFairySouls(int fairyKey) {
            collectedFairySouls.add(fairyKey);
        }

        public void addExchangedFairySouls(int fairyKey) {
            if (collectedFairySouls.contains(fairyKey))
                collectedFairySouls.remove(fairyKey);
            exchangedFairySouls.add(fairyKey);
        }

        public FairySoulExchangeLevels getNextExchangeLevel() {
            // divide amount of exchanged souls by 5
            return FairySoulExchangeLevels.getLevel(1 + (exchangedFairySouls.size() / 5));
        }
    }
}
