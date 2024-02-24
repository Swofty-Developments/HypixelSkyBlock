package net.swofty.types.generic.server.attribute.attributes;

import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.types.generic.server.attribute.ServerAttribute;

public class AttributeLong extends ServerAttribute<Long> {
    private static final JacksonSerializer<Long> serializer = new JacksonSerializer<>(Long.class);

    public AttributeLong(String key, Long value) {
        super(key, value, serializer);
    }

    public AttributeLong(String key) {
        super(key, null, serializer);
    }
}