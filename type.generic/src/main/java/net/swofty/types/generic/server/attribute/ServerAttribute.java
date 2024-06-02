package net.swofty.types.generic.server.attribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import net.swofty.service.protocol.Serializer;

public abstract class ServerAttribute<T> {
    private final String key;
    @Getter
    private T value;
    protected Serializer<T> serializer;

    protected ServerAttribute(String key, T value, Serializer<T> serializer) {
        this.key = key;
        this.value = value;
        this.serializer = serializer;
    }

    public String getKey() {
        return key;
    }

    public String getSerializedValue() throws JsonProcessingException {
        return serializer.serialize(value);
    }

    public void deserializeValue(String json) throws JsonProcessingException {
        this.value = serializer.deserialize(json);
    }

    public void setValue(T value) {
        this.value = value;
    }
}