package net.swofty.service.protocol;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

public class JacksonSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public JacksonSerializer(Class<T> clazz) {
        this.mapper = new ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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