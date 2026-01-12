package net.swofty.commons.protocol;

import lombok.SneakyThrows;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

public class JacksonSerializer<T> implements Serializer<T> {
    private final JsonMapper mapper;
    private final Class<T> clazz;

    public JacksonSerializer(Class<T> clazz) {
        this.mapper = JsonMapper.builder()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
        this.clazz = clazz;
    }

    @SneakyThrows
    @Override
    public String serialize(T value) {
        return mapper.writeValueAsString(value);
    }

    @SneakyThrows
    @Override
    public T deserialize(String json) {
        return mapper.readValue(json, clazz);
    }

    @Override
    public T clone(T value) {
        return value;
    }
}