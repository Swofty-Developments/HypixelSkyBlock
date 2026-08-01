package net.swofty.type.ravengardgeneric.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.ravengardgeneric.data.RavengardDatapoint;

public class DatapointRavengardString extends RavengardDatapoint<String> {
    private static final JacksonSerializer<String> serializer = new JacksonSerializer<>(String.class);

    public DatapointRavengardString(String key, String value) {
        super(key, value, serializer);
    }

    public DatapointRavengardString(String key) {
        super(key, null, serializer);
    }
}
