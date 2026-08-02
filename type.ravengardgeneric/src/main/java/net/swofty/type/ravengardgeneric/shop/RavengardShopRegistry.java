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
                List<RavengardShop.Entry> stock = new ArrayList<>();
                for (Map<String, Object> entry : (List<Map<String, Object>>) data.getOrDefault("stock", List.of())) {
                    stock.add(new RavengardShop.Entry(
                            ((Number) entry.get("slot")).intValue(),
                            String.valueOf(entry.get("item")),
                            entry.get("price") instanceof Number price ? price.intValue() : 0,
                            Boolean.TRUE.equals(entry.get("out_of_stock"))));
                }
                SHOPS.put(id, new RavengardShop(id, String.valueOf(data.get("title")),
                        String.valueOf(data.get("banner")), List.copyOf(stock)));
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
