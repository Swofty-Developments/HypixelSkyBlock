package net.swofty.types.generic.data.datapoints;

import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.serializer.PlayerShopDataSerializer;
import net.swofty.types.generic.user.PlayerShopData;

public class DatapointShopData extends Datapoint<PlayerShopData> {
    private static final PlayerShopDataSerializer serializer = new PlayerShopDataSerializer();

    public DatapointShopData(String key, PlayerShopData value) {
        super(key, value, serializer);
    }

    public DatapointShopData(String key) {
        super(key, null, serializer);
    }
}
