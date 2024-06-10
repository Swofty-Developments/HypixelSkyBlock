package net.swofty.commons.protocol.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.ItemAttribute;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class SkyBlockItemDeserializer extends JsonDeserializer<UnderstandableSkyBlockItem> {
    @Override
    public UnderstandableSkyBlockItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        int amount = node.has("amount") ? node.get("amount").asInt() : 1;

        UnderstandableSkyBlockItem item = new UnderstandableSkyBlockItem(amount); // Start with a base SkyBlockItem

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            String key = field.getKey();
            String value = field.getValue().asText();
            ItemAttribute attribute = item.getAttribute(key);
            if (attribute != null) {
                attribute.setValue(attribute.loadFromString(value));
            }
        }

        return item;
    }

    public static UnderstandableSkyBlockItem deserialize(Map<String, Object> object) {
        UnderstandableSkyBlockItem item = new UnderstandableSkyBlockItem((int) object.get("amount")); // Start with a base SkyBlockItem

        for (Map.Entry<String, Object> entry : object.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            ItemAttribute attribute = item.getAttribute(key);
            if (attribute != null) {
                attribute.setValue(attribute.loadFromString(value));
            }
        }

        return item;
    }

    public static UnderstandableSkyBlockItem deserializeJSON(JSONObject object) {
        UnderstandableSkyBlockItem item = new UnderstandableSkyBlockItem(object.getInt("amount")); // Start with a base SkyBlockItem

        for (String key : object.keySet()) {
            // Skip the amount key
            if (key.equals("amount")) {
                continue;
            }

            try {
                String value = object.getString(key);
                ItemAttribute attribute = item.getAttribute(key);
                if (attribute != null) {
                    attribute.setValue(attribute.loadFromString(value));
                }
            } catch (Exception e) {}
        }

        return item;
    }
}