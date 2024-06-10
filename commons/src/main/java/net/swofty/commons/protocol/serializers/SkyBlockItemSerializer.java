package net.swofty.commons.protocol.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.swofty.commons.item.UnderstandableSkyBlockItem;
import net.swofty.commons.item.attribute.ItemAttribute;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class SkyBlockItemSerializer extends JsonSerializer<UnderstandableSkyBlockItem> {
    @Override
    public void serialize(UnderstandableSkyBlockItem value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        // Iterate over each attribute and serialize it as a key-value pair.
        for (ItemAttribute attribute : value.attributes()) {
            String key = attribute.getKey();
            String valueAsString = attribute.saveIntoString();
            gen.writeStringField(key, valueAsString);
        }

        gen.writeNumberField("amount", value.amount());

        gen.writeEndObject();
    }

    public static Map<String, Object> serialize(UnderstandableSkyBlockItem item) {
        JSONObject json = new JSONObject();

        // Iterate over each attribute and serialize it as a key-value pair.
        for (ItemAttribute attribute : item.attributes()) {
            String key = attribute.getKey();
            String valueAsString = attribute.saveIntoString();
            json.put(key, valueAsString);
        }

        json.put("amount", item.amount());

        return json.toMap();
    }

    public static JSONObject serializeJSON(UnderstandableSkyBlockItem item) {
        JSONObject json = new JSONObject();

        // Iterate over each attribute and serialize it as a key-value pair.
        for (ItemAttribute attribute : item.attributes()) {
            String key = attribute.getKey();
            String valueAsString = attribute.saveIntoString();
            json.put(key, valueAsString);
        }

        json.put("amount", item.amount());

        return json;
    }
}