package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.serializer.JacksonSerializer;

public class DatapointInteger extends Datapoint<Integer> {
    private static final JacksonSerializer<Integer> serializer = new JacksonSerializer<>(Integer.class);

    public DatapointInteger(String key, Integer value) {
        super(key, value, serializer);
    }

    public DatapointInteger(String key) {
        super(key, null, serializer);
    }
}
