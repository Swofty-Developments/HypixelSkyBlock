package net.swofty.commons.protocol.objects.bazaar;

import net.swofty.commons.protocol.ProtocolObject;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BazaarGetPendingOrdersProtocolObject
        extends ProtocolObject<
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersMessage,
        BazaarGetPendingOrdersProtocolObject.BazaarGetPendingOrdersResponse> {

    @Override
    public Serializer<BazaarGetPendingOrdersMessage> getSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarGetPendingOrdersMessage v) {
                JSONObject o = new JSONObject();
                o.put("player-uuid", v.playerUUID.toString());
                o.put("profile-uuid", v.profileUUID.toString());
                return o.toString();
            }

            @Override
            public BazaarGetPendingOrdersMessage deserialize(String json) {
                JSONObject o = new JSONObject(json);
                return new BazaarGetPendingOrdersMessage(
                        UUID.fromString(o.getString("player-uuid")),
                        UUID.fromString(o.getString("profile-uuid"))
                );
            }

            @Override
            public BazaarGetPendingOrdersMessage clone(BazaarGetPendingOrdersMessage v) {
                return new BazaarGetPendingOrdersMessage(v.playerUUID, v.profileUUID);
            }
        };
    }

    @Override
    public Serializer<BazaarGetPendingOrdersResponse> getReturnSerializer() {
        return new Serializer<>() {
            @Override
            public String serialize(BazaarGetPendingOrdersResponse v) {
                JSONArray arr = new JSONArray(v.orders.stream().map(order -> {
                    JSONObject o = new JSONObject();
                    o.put("order-id",   order.orderId.toString());
                    o.put("item-name",  order.itemName);
                    o.put("side",       order.side);
                    o.put("price",      order.price);
                    o.put("amount",     order.amount);
                    o.put("profile-uuid", order.profileUUID.toString());
                    return o;
                }).collect(Collectors.toList()));
                return arr.toString();
            }

            @Override
            public BazaarGetPendingOrdersResponse deserialize(String json) {
                JSONArray arr = new JSONArray(json);
                var list = arr.toList().stream().map(obj -> {
                    JSONObject o = new JSONObject((java.util.Map<?,?>)obj);
                    return new PendingOrder(
                            UUID.fromString(o.getString("order-id")),
                            o.getString("item-name"),
                            o.getString("side"),
                            o.getDouble("price"),
                            o.getDouble("amount"),
                            UUID.fromString(o.getString("profile-uuid"))
                    );
                }).collect(Collectors.toList());
                return new BazaarGetPendingOrdersResponse(list);
            }

            @Override
            public BazaarGetPendingOrdersResponse clone(BazaarGetPendingOrdersResponse v) {
                return new BazaarGetPendingOrdersResponse(v.orders);
            }
        };
    }

    @lombok.AllArgsConstructor
    public static class BazaarGetPendingOrdersMessage {
        public UUID playerUUID;
        public UUID profileUUID;
    }

    public record PendingOrder(
            UUID   orderId,
            String itemName,
            String side,
            double price,
            double amount,
            UUID   profileUUID
    ) {}

    /** Response is just a list of those. */
    @lombok.AllArgsConstructor
    public static class BazaarGetPendingOrdersResponse {
        public List<PendingOrder> orders;
    }
}
