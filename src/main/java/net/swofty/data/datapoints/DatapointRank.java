package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.data.serializer.JacksonSerializer;
import net.swofty.user.categories.Rank;

public class DatapointRank extends Datapoint<Rank> {
    private static final JacksonSerializer<Rank> serializer = new JacksonSerializer<>(Rank.class);

    public DatapointRank(String key, Rank value) {
        super(key, value, serializer);
    }

    public DatapointRank(String key) {
        super(key, null, serializer);
    }
}