package net.swofty.types.generic.data.datapoints;

import net.swofty.commons.PlayerShopData;
import net.swofty.commons.protocol.serializers.PlayerShopDataSerializer;
import net.swofty.types.generic.data.Datapoint;

public class DatapointShopData extends Datapoint<PlayerShopData> {
    private static final PlayerShopDataSerializer serializer = new PlayerShopDataSerializer();

    public DatapointShopData(String key, PlayerShopData value) {
        super(key, value, serializer);
    }

    public DatapointShopData(String key) {
        super(key, null, serializer);
    }
}
