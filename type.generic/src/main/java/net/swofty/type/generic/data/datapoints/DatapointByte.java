package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

public class DatapointByte extends Datapoint<Byte> {
    private static final JacksonSerializer<Byte> serializer = new JacksonSerializer<>(Byte.class);

    public DatapointByte(String key, Byte value) {
        super(key, value, serializer);
    }

    public DatapointByte(String key) {
        super(key, null, serializer);
    }
}
