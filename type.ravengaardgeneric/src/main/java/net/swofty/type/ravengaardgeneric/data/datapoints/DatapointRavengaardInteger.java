package net.swofty.type.ravengaardgeneric.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.ravengaardgeneric.data.RavengaardDatapoint;

public class DatapointRavengaardInteger extends RavengaardDatapoint<Integer> {
    private static final JacksonSerializer<Integer> serializer = new JacksonSerializer<>(Integer.class);

    public DatapointRavengaardInteger(String key, Integer value) {
        super(key, value, serializer);
    }

    public DatapointRavengaardInteger(String key) {
        super(key, null, serializer);
    }
}
