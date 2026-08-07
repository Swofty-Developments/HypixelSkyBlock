package net.swofty.type.ravengardgeneric.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.ravengardgeneric.data.RavengardDatapoint;

public class DatapointRavengardBoolean extends RavengardDatapoint<Boolean> {
    private static final JacksonSerializer<Boolean> serializer = new JacksonSerializer<>(Boolean.class);

    public DatapointRavengardBoolean(String key, Boolean value) {
        super(key, value, serializer);
    }

    public DatapointRavengardBoolean(String key) {
        super(key, null, serializer);
    }
}
