package net.swofty.type.skyblockgeneric.item.handlers.customstatistics;

import net.swofty.commons.skyblock.statistics.ItemStatistics;

import java.util.HashMap;
import java.util.Map;

public class CustomStatisticsRegistry {
    private static final Map<String, CustomStatisticsConfig> REGISTERED_HANDLERS = new HashMap<>();

    static {
        register("SANDBOX_ITEM", new CustomStatisticsConfig((item) -> {
            if (item == null) return ItemStatistics.empty();
            return item.getAttributeHandler().getSandboxData().getStatistics();
        }));
    }

    public static void register(String id, CustomStatisticsConfig handler) {
        REGISTERED_HANDLERS.put(id, handler);
    }

    public static CustomStatisticsConfig getHandler(String id) {
        return REGISTERED_HANDLERS.get(id);
    }
}