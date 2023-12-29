package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.serializer.JacksonSerializer;

public class DatapointLong extends Datapoint<Long> {
    private static final JacksonSerializer<Long> serializer = new JacksonSerializer<>(Long.class);

    public DatapointLong(String key, Long value) {
        super(key, value, serializer);
    }

    public DatapointLong(String key) {
        super(key, null, serializer);
    }
}
