package net.swofty.service.bazaar;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.swofty.service.bazaar.BazaarMarket.LimitOrder;
import org.bson.Document;

import java.time.Instant;
import java.util.UUID;

public class OrderRepository {

    public static void saveNew(LimitOrder o, String itemName) {
        Document d = new Document("_id", o.orderId.toString())
                .append("itemName", itemName)
                .append("owner",   o.owner.toString())
                .append("profileUuid", o.profileUuid.toString())
                .append("side",    o.side.name())
                .append("price",   o.price)
                .append("remaining", o.remaining)
                .append("ts",      o.ts.toString());
        OrderDatabase.ordersCollection.insertOne(d);
    }

    public static void updateRemaining(UUID id, double rem) {
        OrderDatabase.ordersCollection.updateOne(
                Filters.eq("_id", id.toString()),
                Updates.set("remaining", rem)
        );
    }

    public static void delete(UUID id) {
        OrderDatabase.ordersCollection.deleteOne(Filters.eq("_id", id.toString()));
    }

    public static void loadAll() {
        for (Document d : OrderDatabase.ordersCollection.find()) {
            UUID   orderId     = UUID.fromString(d.getString("_id"));
            String itemName    = d.getString("itemName");
            UUID   owner       = UUID.fromString(d.getString("owner"));
            UUID   profileUuid = UUID.fromString(d.getString("profileUuid"));
            LimitOrder.Side side = LimitOrder.Side.valueOf(d.getString("side"));
            double price       = d.getDouble("price");
            double rem         = d.getDouble("remaining");
            Instant ts         = Instant.parse(d.getString("ts"));

            // Reconstruct the LimitOrder with profile UUID and inject into BazaarMarket
            var lo = new LimitOrder(orderId, owner, profileUuid, side, price, rem, ts);
            BazaarMarket.get().injectLoadedOrder(itemName, lo);
        }
    }
}