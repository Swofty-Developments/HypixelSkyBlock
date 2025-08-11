package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

public class DatapointDouble extends Datapoint<Double> {
    private static final JacksonSerializer<Double> serializer = new JacksonSerializer<>(Double.class);

    public DatapointDouble(String key, Double value) {
        super(key, value, serializer);
    }

    public DatapointDouble(String key) {
        super(key, null, serializer);
    }
}