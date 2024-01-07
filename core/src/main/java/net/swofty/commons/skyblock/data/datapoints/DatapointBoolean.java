package net.swofty.commons.skyblock.data.datapoints;

import net.swofty.commons.skyblock.data.Datapoint;
import net.swofty.commons.skyblock.serializer.JacksonSerializer;

public class DatapointBoolean extends Datapoint<Boolean> {
    private static final JacksonSerializer<Boolean> serializer = new JacksonSerializer<>(Boolean.class);

    public DatapointBoolean(String key, Boolean value) {
        super(key, value, serializer);
    }

    public DatapointBoolean(String key) {
        super(key, null, serializer);
    }
}