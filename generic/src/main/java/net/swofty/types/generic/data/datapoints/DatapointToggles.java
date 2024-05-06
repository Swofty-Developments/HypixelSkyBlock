package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;

import java.util.HashMap;
import java.util.Map;

public class DatapointToggles extends Datapoint<DatapointToggles.Toggles> {

    public DatapointToggles(String key, DatapointToggles.Toggles value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(Toggles value) {
                StringBuilder builder = new StringBuilder();

                for (Map.Entry<Toggles.ToggleType, Boolean> entry : value.toggles.entrySet()) {
                    builder.append(entry.getKey().name()).append(":").append(entry.getValue()).append(",");
                }

                return builder.toString();
            }

            @Override
            public Toggles deserialize(String json) {
                Toggles toggles = new Toggles(new HashMap<>());
                String[] split = json.split(",");

                if (split.length == 1 && split[0].isEmpty()) {
                    return toggles;
                }

                for (String s : split) {
                    String[] split1 = s.split(":");
                    toggles.toggles.put(Toggles.ToggleType.valueOf(split1[0]), Boolean.parseBoolean(split1[1]));
                }

                return toggles;
            }

            @Override
            public Toggles clone(Toggles value) {
                return new Toggles(new HashMap<>(value.toggles));
            }
        });
    }

    public DatapointToggles(String key) {
        this(key, new Toggles(new HashMap<>()));
    }

    @AllArgsConstructor
    public static class Toggles {
        private Map<ToggleType, Boolean> toggles;

        public boolean get(ToggleType type) {
            return toggles.getOrDefault(type, type.defaultValue);
        }

        public void set(ToggleType type, boolean value) {
            toggles.put(type, value);
        }

        public void inverse(ToggleType type) {
            toggles.put(type, !toggles.getOrDefault(type, type.defaultValue));
        }

        public enum ToggleType {
            SKYBLOCK_LEVELS_IN_CHAT(true),
            DISABLE_DROP_MESSAGES(false),
            HAS_SPOKEN_TO_BEA(false),
            HAS_SPOKEN_TO_TIA(false),
            HAS_SPOKEN_TO_CURATOR(false),
            HAS_SPOKEN_TO_MADAME_ELEANOR(false),
            HAS_DONE_COAL_TRADE_WITH_BLACKSMITH(false),
            HAS_SPOKEN_TO_SEYMOUR(false),
            HAS_SPOKEN_TO_FISH_MERCHANT(false),
            HAS_SPOKEN_TO_FARM_MERCHANT(false),
            HAS_SPOKEN_TO_ADVENTURER(false),
            HAS_SPOKEN_TO_LUMBER_MERCHANT(false),
            HAS_SPOKEN_TO_MINE_MERCHANT(false),
            HAS_SPOKEN_TO_WEAPONSMITH(false),
            HAS_SPOKEN_TO_BUILDER(false),
            HAS_SPOKEN_TO_WOOL_WEAVER(false),
            HAS_SPOKEN_TO_MAD_REDSTONE_ENGINEER(false),
            HAS_SPOKEN_TO_ZOG(false),
            PAPER_ICONS(false),
            ;

            private final boolean defaultValue;

            ToggleType(boolean defaultValue) {
                this.defaultValue = defaultValue;
            }
        }
    }
}
