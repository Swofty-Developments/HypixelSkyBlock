package net.swofty.server.attribute.attributes;

import net.swofty.serializer.JacksonSerializer;
import net.swofty.server.attribute.ServerAttribute;

public class AttributeLong extends ServerAttribute<Long> {
    private static final JacksonSerializer<Long> serializer = new JacksonSerializer<>(Long.class);

    public AttributeLong(String key, Long value) {
        super(key, value, serializer);
    }

    public AttributeLong(String key) {
        super(key, null, serializer);
    }
}