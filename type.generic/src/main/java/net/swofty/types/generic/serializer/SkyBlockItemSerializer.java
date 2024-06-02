package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.swofty.types.generic.item.SkyBlockItem;
import net.swofty.types.generic.item.attribute.ItemAttribute;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class SkyBlockItemSerializer extends JsonSerializer<SkyBlockItem> {
    @Override
    public void serialize(SkyBlockItem value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        // Iterate over each attribute and serialize it as a key-value pair.
        for (ItemAttribute attribute : value.attributes) {
            String key = attribute.getKey();
            String valueAsString = attribute.saveIntoString();
            gen.writeStringField(key, valueAsString);
        }

        gen.writeNumberField("amount", value.getAmount());

        gen.writeEndObject();
    }

    public static Map<String, Object> serialize(SkyBlockItem item) {
        JSONObject json = new JSONObject();

        // Iterate over each attribute and serialize it as a key-value pair.
        for (ItemAttribute attribute : item.attributes) {
            String key = attribute.getKey();
            String valueAsString = attribute.saveIntoString();
            json.put(key, valueAsString);
        }

        json.put("amount", item.getAmount());

        return json.toMap();
    }

    public static JSONObject serializeJSON(SkyBlockItem item) {
        JSONObject json = new JSONObject();

        // Iterate over each attribute and serialize it as a key-value pair.
        for (ItemAttribute attribute : item.attributes) {
            String key = attribute.getKey();
            String valueAsString = attribute.saveIntoString();
            json.put(key, valueAsString);
        }

        json.put("amount", item.getAmount());

        return json;
    }
}