package net.swofty.type.ravengardgeneric.shop;

import net.swofty.commons.YamlFileUtils;
import org.tinylog.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RavengardShopRegistry {
    private static final Map<String, RavengardShop> SHOPS = new HashMap<>();
    private static final File SHOPS_DIR = new File("./configuration/ravengard/shops");

    private RavengardShopRegistry() {
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        SHOPS.clear();
        try {
            for (File file : YamlFileUtils.getYamlFiles(SHOPS_DIR)) {
                Map<String, Object> data = YamlFileUtils.loadYaml(file);
                String id = String.valueOf(data.get("id"));
                List<Integer> shelf = new ArrayList<>();
                for (Object slot : (List<Object>) data.getOrDefault("shelf_slots", List.of())) {
                    shelf.add(((Number) slot).intValue());
                }
                List<RavengardShop.PoolEntry> pool = new ArrayList<>();
                for (Map<String, Object> entry : (List<Map<String, Object>>) data.getOrDefault("pool", List.of())) {
                    pool.add(new RavengardShop.PoolEntry(
                            String.valueOf(entry.get("item")),
                            ((Number) entry.get("price")).intValue()));
                }
                SHOPS.put(id, new RavengardShop(id, String.valueOf(data.get("title")),
                        String.valueOf(data.get("banner")), List.copyOf(shelf), List.copyOf(pool)));
            }
            Logger.info("Loaded {} Ravengard shops", SHOPS.size());
        } catch (Exception exception) {
            Logger.error(exception, "Failed to load Ravengard shops from {}", SHOPS_DIR);
        }
    }

    public static RavengardShop get(String id) {
        return id == null ? null : SHOPS.get(id);
    }
}
