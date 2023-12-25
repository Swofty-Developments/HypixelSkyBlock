package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.serializer.JacksonSerializer;

public class DatapointFloat extends Datapoint<Float> {
    private static final JacksonSerializer<Float> serializer = new JacksonSerializer<>(Float.class);

    public DatapointFloat(String key, Float value) {
        super(key, value, serializer);
    }

    public DatapointFloat(String key) {
        super(key, null, serializer);
    }
}
