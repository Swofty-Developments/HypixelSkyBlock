package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.murdermystery.MurderMysteryLeaderboardPreferences;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

public class DatapointMurderMysteryLeaderboardPreferences extends Datapoint<MurderMysteryLeaderboardPreferences> {
    private static final JacksonSerializer<MurderMysteryLeaderboardPreferences> serializer =
            new JacksonSerializer<>(MurderMysteryLeaderboardPreferences.class);

    public DatapointMurderMysteryLeaderboardPreferences(String key, MurderMysteryLeaderboardPreferences value) {
        super(key, value, serializer);
    }

    public DatapointMurderMysteryLeaderboardPreferences(String key) {
        this(key, MurderMysteryLeaderboardPreferences.defaults());
    }
}
