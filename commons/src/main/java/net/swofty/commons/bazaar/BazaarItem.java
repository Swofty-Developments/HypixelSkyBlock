package net.swofty.commons.bazaar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BazaarItem {
    private final String name;
    private double buyPrice = 0;
    private double sellPrice = 0;

    public String serialize() {
        return name + ":" + buyPrice + ":" + sellPrice;
    }

    public static BazaarItem deserialize(String serialized) {
        String[] split = serialized.split(":");
        return new BazaarItem(split[0], Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }

    public Document toDocument() {
        return new Document()
                .append("_id", name)
                .append("buyPrice", buyPrice)
                .append("sellPrice", sellPrice);
    }

    public static BazaarItem fromDocument(Document document) {
        return new BazaarItem(document.getString("_id"),
                document.getDouble("buyPrice"),
                document.getDouble("sellPrice"));
    }
}
