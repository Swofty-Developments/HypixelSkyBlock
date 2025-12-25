package net.swofty.type.generic.data.datapoints;

import net.swofty.commons.bedwars.LeaderboardPreferences;
import net.swofty.commons.protocol.JacksonSerializer;
import net.swofty.type.generic.data.Datapoint;

public class DatapointLeaderboardPreferences extends Datapoint<LeaderboardPreferences> {
	private static final JacksonSerializer<LeaderboardPreferences> serializer =
			new JacksonSerializer<>(LeaderboardPreferences.class);

	public DatapointLeaderboardPreferences(String key, LeaderboardPreferences value) {
		super(key, value, serializer);
	}

	public DatapointLeaderboardPreferences(String key) {
		this(key, LeaderboardPreferences.defaults());
	}
}
