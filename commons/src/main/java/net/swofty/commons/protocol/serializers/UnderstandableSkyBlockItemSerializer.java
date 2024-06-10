package net.swofty.commons.protocol.serializers;

import net.minestom.server.item.Material;
import net.swofty.commons.item.ItemType;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.ItemAttribute;
import net.swofty.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.List;

public class UnderstandableSkyBlockItemSerializer implements Serializer<UnderstandableSkyBlockItem> {

    @Override
    public String serialize(UnderstandableSkyBlockItem value) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("itemKey", value.itemKey());
        jsonObject.put("amount", value.amount());
        jsonObject.put("material", value.material().namespace().asString());

        value.attributes().forEach(attribute -> {
            jsonObject.put(attribute.getKey(), attribute.saveIntoString());
        });

        return jsonObject.toString();
    }

    @Override
    public UnderstandableSkyBlockItem deserialize(String json) {
        JSONObject jsonObject = new JSONObject(json);

        ItemType itemKey = null;
        if (jsonObject.has("itemKey"))
            itemKey = ItemType.valueOf(jsonObject.getString("itemKey"));
        int amount = jsonObject.getInt("amount");
        String material = jsonObject.getString("material");

        List<ItemAttribute> attributes = ItemAttribute.getPossibleAttributes().stream().toList();

        attributes.forEach(attribute -> {
            if (jsonObject.has(attribute.getKey())) {
                attribute.loadFromString(jsonObject.getString(attribute.getKey()));
            }
        });

        return new UnderstandableSkyBlockItem(itemKey,
                attributes,
                amount,
                Material.fromNamespaceId(material));
    }

    @Override
    public UnderstandableSkyBlockItem clone(UnderstandableSkyBlockItem value) {
        return new UnderstandableSkyBlockItem(value.itemKey(), value.attributes(), value.amount(), value.material());
    }
}
