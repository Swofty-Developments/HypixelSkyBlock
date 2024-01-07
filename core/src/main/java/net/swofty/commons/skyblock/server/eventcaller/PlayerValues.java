package net.swofty.commons.skyblock.server.eventcaller;

import lombok.Getter;
import net.swofty.commons.skyblock.event.SkyBlockEvent;
import net.swofty.commons.skyblock.event.custom.PlayerRegionChangeEvent;
import net.swofty.commons.skyblock.region.RegionType;
import net.swofty.commons.skyblock.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PlayerValues {
    private final Map<Value, Object> cache = new HashMap<>();

    public PlayerValues(SkyBlockPlayer player, List<Value> values) {
        for (Value value : values) {
            cache.put(value, value.function.apply(player));
        }
    }

    public List<Value> getDifferentValues(PlayerValues other) {
        return cache.keySet().stream()
                .filter(value -> {
                    Object thisValue = cache.get(value);
                    Object otherValue = other.cache.get(value);
                    // Check for equality considering nulls.
                    return !Objects.equals(thisValue, otherValue);
                })
                .toList();
    }

    public Object getValue(Value value) {
        return cache.get(value);
    }

    @Getter
    public enum Value {
        REGION_TYPE(RegionType.class, (player) -> {
            if (player.getRegion() == null) return null;
            return player.getRegion().getType();
        }, (player, regionTypes) -> {
            SkyBlockEvent.callSkyBlockEvent(new PlayerRegionChangeEvent(
                    player,
                    (RegionType) regionTypes.getKey(),
                    (RegionType) regionTypes.getValue()));
        }),
        ;

        private final Class<?> type;
        private final Function<SkyBlockPlayer, ?> function;
        private final BiConsumer<SkyBlockPlayer, Map.Entry<Object, Object>> consumer;

        Value(Class<?> type, Function<SkyBlockPlayer, ?> function, BiConsumer<SkyBlockPlayer, Map.Entry<Object, Object>> consumer) {
            this.type = type;
            this.function = function;
            this.consumer = consumer;
        }

        public static List<Value> getValues() {
            return List.of(values());
        }
    }

}