package net.swofty.data.datapoints;

import net.swofty.data.Datapoint;
import net.swofty.serializer.PlayerShopDataSerializer;
import net.swofty.user.PlayerShopData;

public class DatapointShopData extends Datapoint<PlayerShopData>
{
      private static final PlayerShopDataSerializer serializer = new PlayerShopDataSerializer();

      public DatapointShopData(String key, PlayerShopData value) {
            super(key, value, serializer);
      }

      public DatapointShopData(String key) {
            super(key, null, serializer);
      }
}
