package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.minestom.server.item.Material;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class SkyBlockItemDeserializer extends JsonDeserializer<SkyBlockItem> {
    @Override
    public SkyBlockItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        SkyBlockItem item = new SkyBlockItem(Material.AIR); // Start with a base SkyBlockItem

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            String key = field.getKey();
            String value = field.getValue().asText();
            ItemAttribute attribute = (ItemAttribute) item.getAttribute(key);
            if (attribute != null) {
                attribute.setValue(attribute.loadFromString(value));
            }
        }

        int amount = node.has("amount") ? node.get("amount").asInt() : 1;
        item.setAmount(amount);

        return item;
    }

    public static SkyBlockItem deserialize(Map<String, Object> object) {
        SkyBlockItem item = new SkyBlockItem(Material.AIR); // Start with a base SkyBlockItem

        for (Map.Entry<String, Object> entry : object.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            ItemAttribute attribute = (ItemAttribute) item.getAttribute(key);
            if (attribute != null) {
                attribute.setValue(attribute.loadFromString(value));
            }
        }

        item.setAmount((int) object.get("amount"));

        return item;
    }

    public static SkyBlockItem deserializeJSON(JSONObject object) {
        SkyBlockItem item = new SkyBlockItem(Material.AIR); // Start with a base SkyBlockItem

        for (String key : object.keySet()) {
            // Skip the amount key
            if (key.equals("amount")) {
                continue;
            }

            try {
                String value = object.getString(key);
                ItemAttribute attribute = (ItemAttribute) item.getAttribute(key);
                if (attribute != null) {
                    attribute.setValue(attribute.loadFromString(value));
                }
            } catch (Exception e) {}
        }

        item.setAmount(object.getInt("amount"));

        return item;
    }
}