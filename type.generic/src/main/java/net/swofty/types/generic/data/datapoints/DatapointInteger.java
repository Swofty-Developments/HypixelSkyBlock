package net.swofty.types.generic.data.datapoints;

import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.types.generic.data.Datapoint;

public class DatapointInteger extends Datapoint<Integer> {
    private static final JacksonSerializer<Integer> serializer = new JacksonSerializer<>(Integer.class);

    public DatapointInteger(String key, Integer value) {
        super(key, value, serializer);
    }

    public DatapointInteger(String key) {
        super(key, null, serializer);
    }
}
