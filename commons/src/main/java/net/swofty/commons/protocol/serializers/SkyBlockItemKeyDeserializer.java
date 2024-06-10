package net.swofty.commons.protocol.serializers;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class SkyBlockItemKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return new SkyBlockItemDeserializer().deserialize(ctxt.getParser(), ctxt);
    }
}