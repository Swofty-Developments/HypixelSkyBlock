package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;

import java.util.UUID;

public class DatapointUUID extends SkyBlockDatapoint<UUID> {
    private static final JacksonSerializer<UUID> serializer = new JacksonSerializer<>(UUID.class);

    public DatapointUUID(String key, UUID value) {
        super(key, value, serializer);
    }

    public DatapointUUID(String key) {
        super(key, null, serializer);
    }
}