package net.swofty.types.generic.data.datapoints;

import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.serializer.JacksonSerializer;

public class DatapointLong extends Datapoint<Long> {
    private static final JacksonSerializer<Long> serializer = new JacksonSerializer<>(Long.class);

    public DatapointLong(String key, Long value) {
        super(key, value, serializer);
    }

    public DatapointLong(String key) {
        super(key, null, serializer);
    }
}
