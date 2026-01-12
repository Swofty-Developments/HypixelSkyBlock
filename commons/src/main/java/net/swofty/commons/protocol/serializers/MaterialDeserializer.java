package net.swofty.commons.protocol.serializers;

import com.google.gson.*;
import net.minestom.server.item.Material;

import java.lang.reflect.Type;

public class MaterialDeserializer implements JsonDeserializer<Material> {
    @Override
    public Material deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Material.fromKey(jsonElement.getAsString());
    }
}