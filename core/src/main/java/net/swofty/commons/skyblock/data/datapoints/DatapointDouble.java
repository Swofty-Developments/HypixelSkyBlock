package net.swofty.commons.skyblock.data.datapoints;

import net.swofty.commons.skyblock.data.Datapoint;
import net.swofty.commons.skyblock.serializer.JacksonSerializer;

public class DatapointDouble extends Datapoint<Double> {
    private static final JacksonSerializer<Double> serializer = new JacksonSerializer<>(Double.class);

    public DatapointDouble(String key, Double value) {
        super(key, value, serializer);
    }

    public DatapointDouble(String key) {
        super(key, null, serializer);
    }
}