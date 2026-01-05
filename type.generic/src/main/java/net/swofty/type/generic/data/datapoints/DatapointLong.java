package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.Serializer;
import net.swofty.type.generic.data.Datapoint;

public class DatapointLong extends Datapoint<Long> {
    private static final Serializer<Long> SERIALIZER = new Serializer<>() {
        @Override
        public String serialize(Long value) {
            return value.toString();
        }

        @Override
        public Long deserialize(String json) {
            return Long.parseLong(json);
        }

        @Override
        public Long clone(Long value) {
            return value;
        }
    };

    public DatapointLong(String key, Long value) {
        super(key, value, SERIALIZER);
    }

    public DatapointLong(String key) {
        this(key, 0L);
    }
}

