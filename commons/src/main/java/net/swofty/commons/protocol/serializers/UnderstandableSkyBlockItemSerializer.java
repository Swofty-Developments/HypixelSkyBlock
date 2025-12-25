package net.swofty.commons.protocol.serializers;

import net.minestom.server.item.Material;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.UnderstandableSkyBlockItem;
import net.swofty.commons.skyblock.item.attribute.ItemAttribute;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UnderstandableSkyBlockItemSerializer implements Serializer<UnderstandableSkyBlockItem> {

    @Override
    public String serialize(UnderstandableSkyBlockItem value) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("itemKey", value.itemKey() != null ? value.itemKey().name() : "null");
        jsonObject.put("amount", value.amount());
        jsonObject.put("material", value.itemKey() != null ? value.itemKey().material :
                value.material().key().asString());

        for (ItemAttribute attribute : value.attributes()) {
            jsonObject.put("attr-" + attribute.getKey(), attribute.saveIntoString());
        }

        return jsonObject.toString();
    }

    @Override
    public UnderstandableSkyBlockItem deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        ItemType itemKey = null;
        if (!jsonObject.getString("itemKey").equals("null")) {
            try {
                itemKey = ItemType.valueOf(jsonObject.getString("itemKey"));
            } catch (IllegalArgumentException e) {
                // Handle invalid itemKey
                System.err.println("Invalid itemKey: " + jsonObject.getString("itemKey"));
            }
        }

        int amount = jsonObject.getInt("amount");
        String materialStr = jsonObject.getString("material");
        Material material = Material.fromKey(materialStr);
        if (material == null) {
            throw new IllegalArgumentException("Invalid material: " + materialStr);
        }

        List<ItemAttribute> attributes = new ArrayList<>();
        for (ItemAttribute possibleAttribute : ItemAttribute.getPossibleAttributes()) {
            if (jsonObject.has("attr-" + possibleAttribute.getKey())) {
                Object value = possibleAttribute.loadFromString(jsonObject.getString("attr-" + possibleAttribute.getKey()));
                possibleAttribute.setValue(value);
                attributes.add(possibleAttribute);
            } else {
                possibleAttribute.setValue(possibleAttribute.getDefaultValue(null));
                attributes.add(possibleAttribute);
            }
        }

        return new UnderstandableSkyBlockItem(itemKey, attributes, amount, material);
    }

    @Override
    public UnderstandableSkyBlockItem clone(UnderstandableSkyBlockItem value) {
        return new UnderstandableSkyBlockItem(value.itemKey(), value.attributes(), value.amount(), value.material());
    }
}