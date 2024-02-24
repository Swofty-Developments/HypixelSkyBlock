package net.swofty.commons.bazaar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bson.Document;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class BazaarItem {
    private final String name;
    private Map<UUID, Double> buyOrders;
    private Map<UUID, Double> sellOrders;

    @JsonCreator
    public BazaarItem(@JsonProperty("name") String name,
                      @JsonProperty("buyOrders") Map<UUID, Double> buyOrders,
                      @JsonProperty("sellOrders") Map<UUID, Double> sellOrders) {
        this.name = name;
        this.buyOrders = buyOrders;
        this.sellOrders = sellOrders;
    }

    public String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public BazaarStatistics getBuyStatistics() {
        return new BazaarStatistics(buyOrders);
    }

    public BazaarStatistics getSellStatistics() {
        return new BazaarStatistics(sellOrders);
    }

    public static BazaarItem deserialize(String serialized) {
        Gson gson = new Gson();
        return gson.fromJson(serialized, BazaarItem.class);
    }

    public Document toDocument() {
        return new Document()
                .append("_id", name)
                .append("buyOrders", buyOrders)
                .append("sellOrders", sellOrders);
    }

    public static BazaarItem fromDocument(Document document) {
        String name = document.getString("_id");
        Map<UUID, Double> buyOrders = (Map<UUID, Double>) document.get("buyOrders");
        Map<UUID, Double> sellOrders = (Map<UUID, Double>) document.get("sellOrders");
        return new BazaarItem(name, buyOrders, sellOrders);
    }

    public record BazaarStatistics(Map<UUID, Double> orders) {
        public double getTotalAmount() {
            return orders.values().stream().mapToDouble(Double::doubleValue).sum();
        }

        public double getMeanOrder() {
            return orders.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }

        public double getLowestOrder() {
            return orders.values().stream().min(Double::compareTo).orElse(0.0);
        }

        public double getHighestOrder() {
            return orders.values().stream().max(Double::compareTo).orElse(0.0);
        }
    }
}
