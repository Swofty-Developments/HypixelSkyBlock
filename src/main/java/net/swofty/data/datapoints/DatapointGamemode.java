package net.swofty.data.datapoints;

import net.minestom.server.entity.GameMode;
import net.swofty.data.Datapoint;
import net.swofty.serializer.JacksonSerializer;
import net.swofty.user.categories.Rank;

public class DatapointGamemode extends Datapoint<GameMode> {
    private static final JacksonSerializer<GameMode> serializer = new JacksonSerializer<>(GameMode.class);

    public DatapointGamemode(String key, GameMode value) {
        super(key, value, serializer);
    }

    public DatapointGamemode(String key) {
        super(key, null, serializer);
    }
}