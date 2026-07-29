package net.swofty.commons.protocol;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class JacksonSerializer<T> implements Serializer<T> {
    private static final ObjectMapper MAPPER = JsonMapper.builder()
            .findAndAddModules()
            .build();

    private final Class<T> type;

    public JacksonSerializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public String serialize(T value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize " + type.getSimpleName(), e);
        }
    }

    @Override
    public T deserialize(String json) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize " + type.getSimpleName() + ": " + json, e);
        }
    }

    @Override
    public T clone(T value) {
        return deserialize(serialize(value));
    }
}
