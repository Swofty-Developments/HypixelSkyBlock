package net.swofty.types.generic.server.eventcaller;

import lombok.Getter;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.PlayerRegionChangeEvent;
import net.swofty.types.generic.item.set.ArmorSetRegistry;
import net.swofty.types.generic.item.set.impl.SetEvents;
import net.swofty.types.generic.region.RegionType;
import net.swofty.types.generic.user.SkyBlockPlayer;

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
            SkyBlockEventHandler.callSkyBlockEvent(new PlayerRegionChangeEvent(
                    player,
                    (RegionType) regionTypes.getKey(),
                    (RegionType) regionTypes.getValue()));
        }),
        ARMOR_SET(ArmorSetRegistry.class, SkyBlockPlayer::getArmorSet, (player, armorSet) -> {
            ArmorSetRegistry oldSet = (ArmorSetRegistry) armorSet.getKey();
            ArmorSetRegistry newSet = (ArmorSetRegistry) armorSet.getValue();

            try {
                if (oldSet != null && oldSet.getClazz().newInstance() instanceof SetEvents setEvents) {
                    setEvents.setTakeOff(player);
                }

                if (newSet != null && newSet.getClazz().newInstance() instanceof SetEvents setEvents) {
                    setEvents.setPutOn(player);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
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