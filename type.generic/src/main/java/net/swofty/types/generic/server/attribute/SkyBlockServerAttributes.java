package net.swofty.types.generic.server.attribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.types.generic.SkyBlockConst;
import net.swofty.types.generic.SkyBlockTypeLoader;
import net.swofty.types.generic.calendar.SkyBlockCalendar;
import net.swofty.types.generic.data.mongodb.AttributeDatabase;
import net.swofty.types.generic.server.attribute.attributes.AttributeLong;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class SkyBlockServerAttributes {
    private static final Map<Attributes, ServerAttribute> serverAttributes = new HashMap<>();

    public static void loadAttributes(Document document) throws JsonProcessingException {
        Arrays.stream(Attributes.values()).forEach(data -> {
            if (document == null || !document.containsKey(data.getKey())) {
                serverAttributes.put(data, data.getDefaultAttribute());
                return;
            }

            String jsonValue = document.getString(data.getKey());

            try {
                ServerAttribute<?> attribute = data.getDefaultAttribute().getClass().getDeclaredConstructor(String.class).newInstance(data.getKey());
                attribute.deserializeValue(jsonValue);
                serverAttributes.put(data, attribute);
                data.onLoad.accept(attribute);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static Document toDocument() {
        Document document = new Document();
        document.append("_id", "attributes");
        serverAttributes.forEach((key, attribute) -> {
            attribute.setValue(key.onSave.apply(SkyBlockConst.getTypeLoader()).getValue());

            try {
                document.append(key.getKey(), attribute.getSerializedValue());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return document;
    }

    public <R extends ServerAttribute<T>, T> R get(Attributes attribute, Class<R> type) {
        if (!serverAttributes.containsKey(attribute)) {
            return type.cast(attribute.getDefaultAttribute());
        }
        return type.cast(serverAttributes.get(attribute));
    }

    public static void saveAttributeLoop() {
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            AttributeDatabase.saveDocument(toDocument());
        }, TaskSchedule.seconds(3), TaskSchedule.seconds(3) , ExecutionType.ASYNC);
    }

    public enum Attributes {
        CALENDER_ELAPSED_TIME("elapsed", AttributeLong.class, new AttributeLong("elapsed", 0L), (attribute) -> {
            SkyBlockCalendar.setElapsed((Long) attribute.getValue());
        }, (server) -> new AttributeLong("elapsed", SkyBlockCalendar.getElapsed()));

        @Getter
        private final String key;
        @Getter
        private final Class<? extends ServerAttribute> type;
        @Getter
        private final ServerAttribute defaultAttribute;
        public final Consumer<ServerAttribute> onLoad;
        public final Function<SkyBlockTypeLoader, ServerAttribute> onSave;

        Attributes(String key, Class<? extends ServerAttribute> type, ServerAttribute defaultAttribute, Consumer<ServerAttribute> onLoad, Function<SkyBlockTypeLoader, ServerAttribute> onSave) {
            this.key = key;
            this.type = type;
            this.defaultAttribute = defaultAttribute;
            this.onLoad = onLoad;
            this.onSave = onSave;
        }

        public static Attributes fromKey(String key) {
            for (Attributes data : values()) {
                if (data.getKey().equals(key)) {
                    return data;
                }
            }
            return null;
        }
    }
}
