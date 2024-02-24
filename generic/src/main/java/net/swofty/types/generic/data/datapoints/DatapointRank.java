package net.swofty.types.generic.data.datapoints;

import net.swofty.service.protocol.JacksonSerializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.user.categories.Rank;

public class DatapointRank extends Datapoint<Rank> {
    private static final JacksonSerializer<Rank> serializer = new JacksonSerializer<>(Rank.class);

    public DatapointRank(String key, Rank value) {
        super(key, value, serializer);
    }

    public DatapointRank(String key) {
        super(key, null, serializer);
    }
}