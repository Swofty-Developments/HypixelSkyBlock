package net.swofty.service.store;

import net.swofty.commons.protocol.objects.proxy.to.StorePurchaseFulfillmentProtocol;
import org.bson.Document;

public record StoreEntitlement(String type, String key, long amount, Long durationDays) {
    public static StoreEntitlement fromDocument(Document document) {
        Number amount = document.get("amount", Number.class);
        Number durationDays = document.get("durationDays", Number.class);

        return new StoreEntitlement(
                document.getString("type"),
                document.getString("key"),
                amount != null ? amount.longValue() : 1L,
                durationDays != null ? durationDays.longValue() : null
        );
    }

    public StorePurchaseFulfillmentProtocol.Entitlement toProtocol() {
        return new StorePurchaseFulfillmentProtocol.Entitlement(type, key, amount, durationDays);
    }
}
