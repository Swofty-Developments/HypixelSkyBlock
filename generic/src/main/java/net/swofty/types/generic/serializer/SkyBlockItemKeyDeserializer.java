package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class SkyBlockItemKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        // Implement the logic to deserialize the key string into a SkyBlockItem instance
        // You can use the existing SkyBlockItemDeserializer or create a new one
        return new SkyBlockItemDeserializer().deserialize(ctxt.getParser(), ctxt);
    }
}