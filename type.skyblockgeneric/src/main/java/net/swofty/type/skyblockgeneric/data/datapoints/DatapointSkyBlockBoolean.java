package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.SkyBlockDatapoint;

public class DatapointSkyBlockBoolean extends SkyBlockDatapoint<Boolean> {
    private static final JacksonSerializer<Boolean> serializer = new JacksonSerializer<>(Boolean.class);

    public DatapointSkyBlockBoolean(String key, Boolean value) {
        super(key, value, serializer);
    }

    public DatapointSkyBlockBoolean(String key) {
        super(key, null, serializer);
    }
}