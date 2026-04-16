package net.swofty.type.skyblockgeneric.server.attribute.attributes;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.skyblockgeneric.server.attribute.ServerAttribute;

public class AttributeString extends ServerAttribute<String> {
    private static final JacksonSerializer<String> serializer = new JacksonSerializer<>(String.class);

    public AttributeString(String key, String value) {
        super(key, value, serializer);
    }

    public AttributeString(String key) {
        super(key, null, serializer);
    }
}
