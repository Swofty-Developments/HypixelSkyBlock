package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

import java.util.HashMap;
import java.util.Map;

public class DatapointMapStringLong extends Datapoint<Map<String, Long>> {
    private static final JacksonSerializer<Map<String, Long>> serializer =
            new JacksonSerializer<>((Class) Map.class);

    public DatapointMapStringLong(String key, Map<String, Long> value) {
        super(key, value, serializer);
    }

    // Required for deepClone() reflection which passes HashMap.class
    public DatapointMapStringLong(String key, HashMap<String, Long> value) {
        super(key, value, serializer);
    }

    public DatapointMapStringLong(String key) {
        super(key, new HashMap<>(), serializer);
    }
}
