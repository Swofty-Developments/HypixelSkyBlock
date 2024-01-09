package net.swofty.types.generic.data.datapoints;

import net.minestom.server.entity.GameMode;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.serializer.JacksonSerializer;

public class DatapointGamemode extends Datapoint<GameMode> {
    private static final JacksonSerializer<GameMode> serializer = new JacksonSerializer<>(GameMode.class);

    public DatapointGamemode(String key, GameMode value) {
        super(key, value, serializer);
    }

    public DatapointGamemode(String key) {
        super(key, null, serializer);
    }
}