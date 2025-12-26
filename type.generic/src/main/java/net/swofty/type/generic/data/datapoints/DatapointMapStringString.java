package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

import java.util.HashMap;
import java.util.Map;

public class DatapointMapStringString extends Datapoint<Map<String, String>> {
    private static final JacksonSerializer<Map<String, String>> serializer =
            new JacksonSerializer<>((Class) Map.class);

    public DatapointMapStringString(String key, Map<String, String> value) {
        super(key, value, serializer);
    }

    // Required for deepClone() reflection which passes HashMap.class
    public DatapointMapStringString(String key, HashMap<String, String> value) {
        super(key, value, serializer);
    }

    public DatapointMapStringString(String key) {
        super(key, new HashMap<>(), serializer);
    }
}
