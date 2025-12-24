package net.swofty.commons.skyblock.item.attribute.attributes;

import lombok.SneakyThrows;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemAttributeBackpackData extends ItemAttribute<ItemAttributeBackpackData.BackpackData> {
    @Override
    public String getKey() {
        return "backpack_data";
    }

    @Override
    public BackpackData getDefaultValue(@Nullable ItemStatistics defaultStatistics) {
        return new BackpackData();
    }

    @SneakyThrows
    @Override
    public ItemAttributeBackpackData.BackpackData loadFromString(String string) {
        JSONObject jsonObject = new JSONObject(string);
        List<UnderstandableSkyBlockItem> items = new ArrayList<>();

        if (jsonObject.has("items")) {
            for (Object item : jsonObject.getJSONArray("items")) {
                items.add(UnderstandableSkyBlockItem.deserialize(item.toString()));
            }
        }

        return new BackpackData(items);
    }

    @SneakyThrows
    @Override
    public String saveIntoString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("items", new JSONArray(value.items().stream()
                .map(UnderstandableSkyBlockItem::serialize)
                .toList()));

        return jsonObject.toString();
    }

    public record BackpackData(List<UnderstandableSkyBlockItem> items) {
        public BackpackData() {
            this(new ArrayList<>());
        }
    }
}

