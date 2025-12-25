package net.swofty.type.skyblockgeneric.data.datapoints;

import net.swofty.commons.skyblock.PlayerShopData;
import net.swofty.commons.protocol.serializers.PlayerShopDataSerializer;
import net.swofty.type.skyblockgeneric.data.SkyBlockDatapoint;

public class DatapointShopData extends SkyBlockDatapoint<PlayerShopData> {
    private static final PlayerShopDataSerializer serializer = new PlayerShopDataSerializer();

    public DatapointShopData(String key, PlayerShopData value) {
        super(key, value, serializer);
    }

    public DatapointShopData(String key) {
        super(key, null, serializer);
    }
}
