package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.minestom.server.item.Material;

import java.io.IOException;

public class MaterialDeserializer extends JsonDeserializer<Material> {
    @Override
    public Material deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return Material.fromNamespaceId(node.asText());
    }
}