package net.swofty.type.ravengardgeneric.item;

import net.swofty.commons.YamlFileUtils;
import net.swofty.type.ravengardgeneric.classes.RavengardClass;
import org.tinylog.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class RavengardItemRegistry {
    private static final Map<String, RavengardItemType> ITEMS = new HashMap<>();
    private static final File ITEMS_DIR = new File("./configuration/ravengard/items");

    private RavengardItemRegistry() {
    }

    public static void load() {
        ITEMS.clear();
        try {
            List<File> files = YamlFileUtils.getYamlFiles(ITEMS_DIR);
            for (File file : files) {
                try {
                    Map<String, Object> data = YamlFileUtils.loadYaml(file);
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
                    if (items == null) {
                        Logger.warn("No items found in {}", file.getName());
                        continue;
                    }
                    for (Map<String, Object> config : items) {
                        try {
                            RavengardItemType type = RavengardItemConfigParser.parse(config);
                            ITEMS.put(type.getId(), type);
                        } catch (Exception exception) {
                            Logger.error(exception, "Failed to parse Ravengard item {}", config.get("id"));
                        }
                    }
                } catch (Exception exception) {
                    Logger.error(exception, "Failed to load {}", file.getName());
                }
            }
            Logger.info("Loaded {} Ravengard items from {} files", ITEMS.size(), files.size());
        } catch (Exception exception) {
            Logger.error(exception, "Failed to read Ravengard item configs from {}", ITEMS_DIR);
        }
    }

    public static RavengardItemType get(String id) {
        return id == null ? null : ITEMS.get(id.toUpperCase());
    }

    public static List<RavengardItemType> all() {
        return new ArrayList<>(ITEMS.values());
    }

    public static List<RavengardItemType> forClass(RavengardClass value) {
        List<RavengardItemType> result = new ArrayList<>();
        for (RavengardItemType type : ITEMS.values()) {
            if (type.usableBy(value)) {
                result.add(type);
            }
        }
        return result;
    }
}
