package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;
import net.swofty.type.generic.leaderboard.LeaderboardTracked;

public class DatapointHypixelExperience extends Datapoint<Long> implements LeaderboardTracked {
    private static final JacksonSerializer<Long> serializer = new JacksonSerializer<>(Long.class);
    private static final String LEADERBOARD_KEY = "hypixel:network_experience";

    public DatapointHypixelExperience(String key, Long value) {
        super(key, value != null ? value : 0L, serializer);
    }

    public DatapointHypixelExperience(String key) {
        super(key, 0L, serializer);
    }

    @Override
    public String getLeaderboardKey() {
        return LEADERBOARD_KEY;
    }

    @Override
    public double getLeaderboardScore() {
        return value != null ? value.doubleValue() : 0.0;
    }
}
