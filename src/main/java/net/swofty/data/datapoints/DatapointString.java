package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.data.serializer.JacksonSerializer;

public class DatapointString extends Datapoint<String> {
    private static final JacksonSerializer<String> serializer = new JacksonSerializer<>(String.class);

    public DatapointString(String key, String value) {
        super(key, value, serializer);
    }

    public DatapointString(String key) {
        super(key, null, serializer);
    }
}