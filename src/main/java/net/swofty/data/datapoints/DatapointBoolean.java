package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.serializer.JacksonSerializer;

public class DatapointBoolean extends Datapoint<Boolean> {
    private static final JacksonSerializer<Boolean> serializer = new JacksonSerializer<>(Boolean.class);

    public DatapointBoolean(String key, Boolean value) {
        super(key, value, serializer);
    }

    public DatapointBoolean(String key) {
        super(key, null, serializer);
    }
}