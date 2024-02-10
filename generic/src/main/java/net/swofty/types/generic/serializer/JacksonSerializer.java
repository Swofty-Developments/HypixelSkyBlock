package net.swofty.types.generic.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import net.swofty.service.generic.Serializer;

public class JacksonSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public JacksonSerializer(Class<T> clazz) {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
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