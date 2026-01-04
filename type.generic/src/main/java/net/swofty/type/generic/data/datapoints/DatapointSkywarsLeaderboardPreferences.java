package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.skywars.SkywarsLeaderboardPreferences;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

public class DatapointSkywarsLeaderboardPreferences extends Datapoint<SkywarsLeaderboardPreferences> {
    private static final JacksonSerializer<SkywarsLeaderboardPreferences> serializer =
            new JacksonSerializer<>(SkywarsLeaderboardPreferences.class);

    public DatapointSkywarsLeaderboardPreferences(String key, SkywarsLeaderboardPreferences value) {
        super(key, value, serializer);
    }

    public DatapointSkywarsLeaderboardPreferences(String key) {
        this(key, SkywarsLeaderboardPreferences.defaults());
    }
}
