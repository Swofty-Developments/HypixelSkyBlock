package net.swofty.types.generic.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.swofty.service.protocol.Serializer;
import net.swofty.types.generic.data.Datapoint;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.serializer.SkyBlockItemDeserializer;
import net.swofty.types.generic.serializer.SkyBlockItemSerializer;
import org.json.JSONObject;

import java.util.Map;

public class DatapointAuctionEscrow extends Datapoint<DatapointAuctionEscrow.AuctionEscrow> {

    public DatapointAuctionEscrow(String key, DatapointAuctionEscrow.AuctionEscrow value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(AuctionEscrow value) {
                JSONObject obj = new JSONObject();
                if (value.getItem() == null) {
                    obj.put("item", new JSONObject());
                } else {
                    obj.put("item", SkyBlockItemSerializer.serialize(value.getItem()));
                }
                obj.put("price", value.getPrice());
                obj.put("duration", value.getDuration());
                obj.put("bin", value.isBin());
                return obj.toString();
            }

            @Override
            public AuctionEscrow deserialize(String json) {
                JSONObject obj = new JSONObject(json);

                if (obj.getJSONObject("item").isEmpty()) {
                    return new AuctionEscrow(null, obj.getLong("price"), obj.getLong("duration"), obj.getBoolean("bin"));
                }

                return new AuctionEscrow(
                        SkyBlockItemDeserializer.deserialize((Map<String, Object>) obj.get("item")),
                        obj.getLong("price"),
                        obj.getLong("duration"),
                        obj.getBoolean("bin"));
            }

            @Override
            public AuctionEscrow clone(AuctionEscrow value) {
                return new AuctionEscrow(value.getItem(), value.getPrice(), value.getDuration(), value.isBin());
            }
        });
    }

    public DatapointAuctionEscrow(String key) {
        this(key, new AuctionEscrow(null, 50L, 21600000L, false));
    }

    public void clearEscrow() {
        setValue(new AuctionEscrow(null, 50L, 21600000L, false));
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class AuctionEscrow {
        private SkyBlockItem item;
        private long price;
        private long duration;
        private boolean isBin;
    }
}
