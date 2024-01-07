package net.swofty.commons.skyblock.server.attribute.attributes;

import net.swofty.commons.skyblock.serializer.JacksonSerializer;
import net.swofty.commons.skyblock.server.attribute.ServerAttribute;

public class AttributeLong extends ServerAttribute<Long> {
    private static final JacksonSerializer<Long> serializer = new JacksonSerializer<>(Long.class);

    public AttributeLong(String key, Long value) {
        super(key, value, serializer);
    }

    public AttributeLong(String key) {
        super(key, null, serializer);
    }
}