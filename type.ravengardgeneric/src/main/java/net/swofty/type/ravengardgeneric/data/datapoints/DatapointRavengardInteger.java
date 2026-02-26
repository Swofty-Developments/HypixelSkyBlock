package net.swofty.type.ravengardgeneric.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.ravengardgeneric.data.RavengardDatapoint;

public class DatapointRavengardInteger extends RavengardDatapoint<Integer> {
    private static final JacksonSerializer<Integer> serializer = new JacksonSerializer<>(Integer.class);

    public DatapointRavengardInteger(String key, Integer value) {
        super(key, value, serializer);
    }

    public DatapointRavengardInteger(String key) {
        super(key, null, serializer);
    }
}
