package net.swofty.types.generic.server.eventcaller;

import kotlin.jvm.functions.Function0;
import lombok.Getter;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.event.SkyBlockEventHandler;
import net.swofty.types.generic.event.custom.CalenderHourlyUpdateEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ServerValues {
    private final Map<Value, Object> cache = new HashMap<>();

    public ServerValues(List<Value> values) {
        for (Value value : values) {
            cache.put(value, value.function.invoke());
        }
    }

    public List<Value> getDifferentValues(ServerValues other) {
        return cache.keySet().stream()
                .filter(serverValue -> !cache.get(serverValue).equals(other.cache.get(serverValue)))
                .toList();
    }

    public Object getValue(Value value) {
        return cache.get(value);
    }

    @Getter
    public enum Value {
        CALENDER_HOUR(Integer.class, SkyBlockCalendar::getHour, (hour) -> {
            SkyBlockEventHandler.callSkyBlockEvent(new CalenderHourlyUpdateEvent((Integer) hour));
        }),
        ;

        private final Class<?> type;
        private final Function0<?> function;
        private final BiFunction<Object, Object, Boolean> shouldCall;
        private final Consumer<Object> consumer;

        Value(Class<?> type, Function0<?> function, Consumer<Object> consumer) {
            this.type = type;
            this.function = function;
            this.shouldCall = (oldValue, newValue) -> !oldValue.equals(newValue);
            this.consumer = consumer;
        }

        public static List<Value> getValues() {
            return List.of(values());
        }
    }
}