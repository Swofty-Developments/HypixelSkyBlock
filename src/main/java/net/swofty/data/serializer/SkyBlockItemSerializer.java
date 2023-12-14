package net.swofty.data.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.swofty.item.SkyBlockItem;
import net.swofty.item.attribute.ItemAttribute;

import java.io.IOException;

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

        gen.writeEndObject();
    }
}