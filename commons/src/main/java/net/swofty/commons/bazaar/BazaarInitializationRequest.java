package net.swofty.commons.bazaar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public record BazaarInitializationRequest(Map<String, Map.Entry<Double, Double>> itemsToInitialize) {
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        itemsToInitialize.forEach((key, value) -> {
            builder.append(key).append(":").append(value.getKey()).append(":").append(value.getValue()).append(",");
        });
        return builder.toString();
    }

    public static BazaarInitializationRequest deserialize(String serialized) {
        Map<String, Map.Entry<Double, Double>> items = new HashMap<>();
        String[] split = serialized.split(",");
        for (String s : split) {
            String[] split1 = s.split(":");
            items.put(split1[0], Map.entry(Double.parseDouble(split1[1]), Double.parseDouble(split1[2])));
        }
        return new BazaarInitializationRequest(items);
    }
}
