package net.swofty.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.minestom.server.item.Material;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.ItemAttribute;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class SkyBlockItemDeserializer extends JsonDeserializer<SkyBlockItem> {
    @Override
    public SkyBlockItem deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
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

        return item;
    }
}