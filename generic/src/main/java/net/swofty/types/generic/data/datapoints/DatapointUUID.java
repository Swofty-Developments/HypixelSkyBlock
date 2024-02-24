package net.swofty.types.generic.data.datapoints;

import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.types.generic.data.Datapoint;

import java.util.UUID;

public class DatapointUUID extends Datapoint<UUID> {
    private static final JacksonSerializer<UUID> serializer = new JacksonSerializer<>(UUID.class);

    public DatapointUUID(String key, UUID value) {
        super(key, value, serializer);
    }

    public DatapointUUID(String key) {
        super(key, null, serializer);
    }
}