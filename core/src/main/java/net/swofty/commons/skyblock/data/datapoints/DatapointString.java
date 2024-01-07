package net.swofty.commons.skyblock.data.datapoints;

import net.swofty.commons.skyblock.data.Datapoint;
import net.swofty.commons.skyblock.serializer.JacksonSerializer;

public class DatapointString extends Datapoint<String> {
    private static final JacksonSerializer<String> serializer = new JacksonSerializer<>(String.class);

    public DatapointString(String key, String value) {
        super(key, value, serializer);
    }

    public DatapointString(String key) {
        super(key, null, serializer);
    }
}